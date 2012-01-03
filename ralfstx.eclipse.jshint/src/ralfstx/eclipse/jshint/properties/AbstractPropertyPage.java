package ralfstx.eclipse.jshint.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;


public abstract class AbstractPropertyPage extends PropertyPage {

  protected static Composite createMainComposite( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( createMainLayout() );
    composite.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    return composite;
  }

  private static GridLayout createMainLayout() {
    GridLayout layout = new GridLayout();
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    return layout;
  }

  protected static Composite createDefaultComposite( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NULL );
    composite.setLayout( new GridLayout( 2, false ) );
    composite.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, true ) );
    return composite;
  }

  protected GridData createSpanGridData() {
    GridData labelData = new GridData( SWT.FILL, SWT.TOP, true, false );
    labelData.horizontalSpan = 2;
    return labelData;
  }

  protected IResource getResource() {
    IAdaptable element = getElement();
    if( element instanceof IResource ) {
      return ( IResource )element;
    }
    return ( IResource )element.getAdapter( IResource.class );
  }

  protected void addSeparator( Composite parent ) {
    Label separator = new Label( parent, SWT.SEPARATOR | SWT.HORIZONTAL );
    separator.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
  }

  protected void hideButtons() {
    getDefaultsButton().setVisible( false );
    getApplyButton().setVisible( false );
  }

  protected boolean getProjectEnabled() throws CoreException {
    IProject project = getResource().getProject();
    return StatusHelper.getProjectEnabled( project );
  }

}