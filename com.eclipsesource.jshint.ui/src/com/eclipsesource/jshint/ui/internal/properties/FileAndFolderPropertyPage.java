/*******************************************************************************
 * Copyright (c) 2012 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial implementation and API
 ******************************************************************************/
package com.eclipsesource.jshint.ui.internal.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.eclipsesource.jshint.ui.internal.Activator;
import com.eclipsesource.jshint.ui.internal.builder.BuilderUtil;
import com.eclipsesource.jshint.ui.internal.builder.JSHintBuilder;
import com.eclipsesource.jshint.ui.internal.preferences.EnablementPreferences;


public class FileAndFolderPropertyPage extends AbstractPropertyPage {

  private Button excludeCheckbox;

  @Override
  public boolean performOk() {
    try {
      boolean changed = storePreferences();
      if( changed ) {
        triggerRebuild();
      }
    } catch( CoreException exception ) {
      String message = "Failed to store settings";
      Activator.logError( message, exception );
      return false;
    }
    return true;
  }

  @Override
  protected void performDefaults() {
    super.performDefaults();
    if( isValid() ) {
      excludeCheckbox.setSelection( false );
    }
  }

  @Override
  protected Control createContents( Composite parent ) {
    Composite composite = createMainComposite( parent );
    addEnablementSection( composite );
    return composite;
  }

  private boolean storePreferences() throws CoreException {
    EnablementPreferences preferences = new EnablementPreferences( getPreferences() );
    String resourcePath = getResourcePath();
    preferences.setExcluded( resourcePath, excludeCheckbox.getSelection() );
    boolean changed = preferences.hasChanged();
    if( changed ) {
      preferences.save();
    }
    return changed;
  }

  private void triggerRebuild() throws CoreException {
    IResource resource = getResource();
    if( resource.getType() == IResource.FILE ) {
      // Files can be rebuilt by touching them
      resource.touch( null );
    } else {
      // For folders, we have to rebuild the project
      // TODO: find a more efficient way to rebuild only the excluded / included folder
      BuilderUtil.triggerClean( resource.getProject(), JSHintBuilder.ID );
    }
  }

  private void addEnablementSection( Composite parent ) {
    EnablementPreferences preferences = new EnablementPreferences( getPreferences() );
    Composite composite = createDefaultComposite( parent );
    Label label = new Label( composite, SWT.NONE );
    label.setLayoutData( createSpanGridData() );
    boolean enabled = preferences.getEnabled();
    label.setText( "The project has JSHint " + ( enabled ? "enabled" : "disabled" ) );
    excludeCheckbox = new Button( composite, SWT.CHECK );
    String fileOrFolder = getResource().getType() == IResource.FILE ? "file" : "folder";
    excludeCheckbox.setText( "Exclude this " + fileOrFolder + " from jshint validation" );
    excludeCheckbox.setSelection( preferences.getExcluded( getResourcePath() ) );
    excludeCheckbox.setEnabled( enabled );
  }

  private String getResourcePath() {
    IResource resource = getResource();
    return EnablementPreferences.getResourcePath( resource );
  }

}
