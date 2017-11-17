package greentea;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.*;

/*
 * Will be changed 
 */

public class GreenTea {
	private org.eclipse.jface.viewers.TreeViewer treeViewer;
	private org.eclipse.jface.viewers.TableViewer tableViewer;

	private void setViewer() {
		
	}
	
	@PostConstruct
	public void createPartControl(Composite parent) {
	    Composite container = new Composite(parent, SWT.NONE);
	    GridLayout gl_container = new GridLayout(2, false);
	    container.setLayout(gl_container);
	    
	    setViewer();
	    
	    treeViewer = new org.eclipse.jface.viewers.TreeViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
	    Tree leftTree = treeViewer.getTree();
	    leftTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	    tableViewer = new org.eclipse.jface.viewers.TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
	    Table rightTable = tableViewer.getTable();
	    rightTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	@Focus
	public void setFocus() {
		treeViewer.getControl().setFocus();

	}
}
