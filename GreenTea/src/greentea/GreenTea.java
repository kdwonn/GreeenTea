package greentea;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;

public class GreenTea {
	private TTreeViewer treeViewer;
	private org.eclipse.jface.viewers.TreeViewer viewer;
	private Real_update updateViewer;
	

	/*
	 * composite parent -> separate to some area and only left area used for treeviewrcol.
	 * 	Will be moved to treeViewer
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {
		treeViewer = new TTreeViewer(parent);
		viewer = treeViewer.getViewer();
		updateViewer = new Real_update();
		
		updateViewer.updating(parent);
		
			
	    viewer.addDoubleClickListener(new IDoubleClickListener() {
        	@Override
            public void doubleClick(DoubleClickEvent event) {
        		org.eclipse.jface.viewers.TreeViewer viewer = (org.eclipse.jface.viewers.TreeViewer) event.getViewer();
                IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
                Object selectedNode = thisSelection.getFirstElement();
                viewer.setExpandedState(selectedNode,!viewer.getExpandedState(selectedNode));
            }
        });
        
	    
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();

	}
}
