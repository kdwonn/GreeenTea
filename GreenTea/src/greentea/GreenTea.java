package greentea;

import java.io.File;
import javax.annotation.PostConstruct;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import java.util.ArrayList;
import java.util.List;

public class GreenTea {
	private org.eclipse.jface.viewers.TreeViewer treeViewer;

	/*
	 * composite parent -> sepatate to some area and only left area used for treeviewrcol.
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {
	    //Composite container = new Composite(parent, SWT.NONE);
	    //GridLayout gl_container = new GridLayout(2, false);
	    //container.setLayout(gl_container);
	    
	    treeViewer = new org.eclipse.jface.viewers.TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FILL);
        treeViewer.setContentProvider(new ViewContentProvider());
        treeViewer.getTree().setHeaderVisible(true);

        TreeViewerColumn mainColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
        mainColumn.getColumn().setText("Projects Structure");
        mainColumn.getColumn().setWidth(300);
        mainColumn.setLabelProvider(new DelegatingStyledCellLabelProvider(new ViewLabelProvider()));

        TreeViewerColumn metric1Column = new TreeViewerColumn(treeViewer, SWT.NONE);
        metric1Column.getColumn().setText("metric1");
        metric1Column.getColumn().setWidth(75);
        metric1Column.getColumn().setAlignment(SWT.RIGHT);
        metric1Column.setLabelProvider(new DelegatingStyledCellLabelProvider(new MetricProvider(1)));
        
        TreeViewerColumn metric2Column = new TreeViewerColumn(treeViewer, SWT.NONE);
        metric2Column.getColumn().setText("metric2");
        metric2Column.getColumn().setWidth(75);
        metric2Column.getColumn().setAlignment(SWT.RIGHT);
        metric2Column.setLabelProvider(new DelegatingStyledCellLabelProvider(new MetricProvider(2)));

	    treeViewer.addDoubleClickListener(new IDoubleClickListener() {
        	@Override
            public void doubleClick(DoubleClickEvent event) {
        		org.eclipse.jface.viewers.TreeViewer viewer = (org.eclipse.jface.viewers.TreeViewer) event.getViewer();
                IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
                Object selectedNode = thisSelection.getFirstElement();
                viewer.setExpandedState(selectedNode,!viewer.getExpandedState(selectedNode));
            }
        });
        
	}
	
	//Will be moved to treeviewre
    class ViewContentProvider implements ITreeContentProvider {
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        @Override
        public void dispose() {
        }

        @Override
        public Object[] getElements(Object inputElement) {
        	return ProjectAnalyser.getProjectNames();
        }

        @Override
        public Object[] getChildren(Object parentElement) {
        	if(parentElement instanceof String) {
        		String projectName = (String)parentElement;
        		List<List<String>> array = new ArrayList<List<String>>();
        		for(String packageName : ProjectAnalyser.getPackageNames(projectName)) {
        			List<String> tmpArray = new ArrayList<String>();
        			tmpArray.add(projectName);
        			tmpArray.add(packageName);
        			array.add(tmpArray);
        		}
        		return (List<String>[])array.toArray();
        	}
        	else {
        		List<String> givenElement = (List<String>)parentElement;
        		int size = givenElement.size();
        		List<List<String>> array = new ArrayList<List<String>>();
        		if(size == 2) {
        			for(String className : ProjectAnalyser.getClassNames(givenElement.get(0), givenElement.get(1))) {
        				List<String> tmpAray = givenElement;
        				tmpAray.add(className);
        				array.add(tmpAray);
        			}
        		}
        		else if (size == 3) {
        			for(String methodName : ProjectAnalyser.getMethodNames(givenElement.get(0), givenElement.get(1), givenElement.get(2))) {
        				List<String> tmpAray = givenElement;
        				tmpAray.add(methodName);
        				array.add(tmpAray);
        			}
        		}
        	}
        	return null;
        }

        @Override
        public Object getParent(Object element) {
        	if(element instanceof String) {
        		return null;
        	}
        	else {
        		List<String> givenElement = (List<String>)element;
        		int size = givenElement.size();
        		if(size == 2) {
        			return givenElement.get(0);
        		}
        		else if (size == 3) {
        			givenElement.remove(2);
        			return givenElement;
        		}
        		else if (size == 4) {
        			givenElement.remove(3);
        			return givenElement;
        		}
        	}
        	return null;
        }

        @Override
        public boolean hasChildren(Object element) {
        	//TODO
        	return true;
        }
    }
    
    
    //TODO
    class ViewLabelProvider extends LabelProvider implements IStyledLabelProvider {
        private ImageDescriptor directoryImage;
        private ResourceManager resourceManager;

        public ViewLabelProvider() {
        }

        @Override
        public StyledString getStyledText(Object element) {
        	String name;
        	if(element instanceof String) {
        		name = (String)element;
        	}
        	else {
        		List<String> array = (List<String>)element;
        		name = array.get(array.size() - 1);
        	}
        	StyledString result = new StyledString(name);        	
            return result;
        }

        @Override
        public Image getImage(Object element) {
            return null;
        }

        @Override
        public void dispose() {
            // garbage collection system resources
            if (resourceManager != null) {
                resourceManager.dispose();
                resourceManager = null;
            }
        }

        protected ResourceManager getResourceManager() {
            if (resourceManager == null) {
                resourceManager = new LocalResourceManager(JFaceResources.getResources());
            }
            return resourceManager;
        }
    }
	
	//TODO
	class MetricProvider extends LabelProvider implements IStyledLabelProvider {
		private ImageDescriptor directoryImage;
		private ResourceManager resourceManager;
		
		public MetricProvider(int index) {
			//TODO match index to metric (functional?)
		}
		
		@Override
        public StyledString getStyledText(Object element) {
            return null;
        }

		@Override
        public Image getImage(Object element) {
            if (element instanceof File) {
                if (((File) element).isDirectory()) {
                    return getResourceManager().createImage(directoryImage);
                }
            }

            return super.getImage(element);
        }

        @Override
        public void dispose() {
            // garbage collection system resources
            if (resourceManager != null) {
                resourceManager.dispose();
                resourceManager = null;
            }
        }

        protected ResourceManager getResourceManager() {
            if (resourceManager == null) {
                resourceManager = new LocalResourceManager(JFaceResources.getResources());
            }
            return resourceManager;
        }
	}
	
	
	

	@Focus
	public void setFocus() {
		treeViewer.getControl().setFocus();

	}
}
