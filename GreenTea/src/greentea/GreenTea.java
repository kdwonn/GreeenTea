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
        treeViewer.setInput(ProjectAnalyser.getProjectNames());
        
        
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
	
	//Will be moved to treeViewer
    class ViewContentProvider implements ITreeContentProvider {
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        @Override
        public void dispose() {
        }

        @Override
        public Object[] getElements(Object inputElement) {
        	if(inputElement instanceof String[]) {
        		String projectNames[] = (String[])inputElement;
        		List<Path> list = new ArrayList<Path>();
        		for(String projectName : projectNames) {
        			list.add(new Path(projectName));
        		}
        		return list.toArray(new Path[0]);
        	}
        	else {
        		return null;
        	}
        }

        @Override
        public Object[] getChildren(Object parentElement) {
        	if(parentElement instanceof Path) {
        		Path path = (Path)parentElement;
        		List<Path> list = new ArrayList<Path>();
        		if(path.getType() == Path.PROJECT) {
        			String projectName = path.getProjectName();
        			for(String packageName : ProjectAnalyser.getPackageNames(projectName)) {
        				list.add(new Path(path, packageName));
        			}
        			return list.toArray(new Path[0]);
        		}
        		else if (path.getType() == Path.PACKAGE) {
        			String projectName = path.getProjectName();
        			String packageName = path.getPackageName();
        			for(String className : ProjectAnalyser.getClassNames(projectName, packageName)) {
        				list.add(new Path(path, className));
        			}
        			return list.toArray(new Path[0]);
        		}
        		else if (path.getType() == Path.CLASS) {
        			String projectName = path.getProjectName();
        			String packageName = path.getPackageName();
        			String className = path.getClassName();
        			for(String methodName : ProjectAnalyser.getMethodNames(projectName, packageName, className)) {
        				list.add(new Path(path, methodName));
        			}
        			return list.toArray(new Path[0]);
        		}
        	}
        	return null;
        }

        @Override
        public Object getParent(Object element) {
        	if (element instanceof Path) {
        		return ((Path) element).getParent();
        	}
        	return null;
        }

        @Override
        public boolean hasChildren(Object element) {
        	if (element instanceof Path) {
        		Path path = (Path)element;
        		if(path.getType() < 1 || path.getType() > 3) {
        			return false;
        		}
        		else {
        			return true;
        		}
        	}
        	return false;
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
        	String name = "";
        	
        	if(element instanceof Path) {
        		Path path = (Path)element;
        		name = path.toString();
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
		
		private final int index;
		
		public MetricProvider(int index) {
			this.index = index;
			//TODO match index to metric (functional?)
		}
		
		@Override
        public StyledString getStyledText(Object element) {
			if(element instanceof Path) {
				return new StyledString(String.valueOf(index));
			}
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
	
	
	class Path {
		static final int PROJECT = 1;
		static final int PACKAGE = 2;
		static final int CLASS = 3;
		static final int METHOD = 4;
		
		private final int type;
		
		private String projectName;
		private String packageName;
		private String className;
		private String methodName;
		
		public Path(String projectName) {
			type = PROJECT;
			this.projectName = projectName;
		}
		
		public Path(String projectName, String packageName) {
			type = PACKAGE;
			this.projectName = projectName;
			this.packageName = packageName;
		}
		
		public Path(String projectName, String packageName, String className) {
			type = CLASS;
			this.projectName = projectName;
			this.packageName = packageName;
			this.className = className;
		}
		
		public Path(String projectName, String packageName, String className, String methodName) {
			type = METHOD;
			this.projectName = projectName;
			this.packageName = packageName;
			this.className = className;
			this.methodName = methodName;
		}
		
		public Path(Path oldPath, String str) {
			int type = oldPath.getType();
			if(type < 4 && type > 0) {
				this.type = type + 1;
				this.projectName = oldPath.getProjectName();
				if(type > 1) {
					this.packageName = oldPath.getPackageName();
				}
				else {
					this.packageName = str;
				}
				if(type > 2) {
					this.className = oldPath.getClassName();
					this.methodName = str;
				}
				else {
					this.className = str;
				}
				
			}
			else {
				this.type = 0;
			}
		}
		
		public Path getParent() {
			if (type == PACKAGE) {
				return new Path(projectName);
			}
			else if (type == CLASS) {
				return new Path(projectName, packageName);
			}
			else if (type == METHOD) {
				return new Path(projectName, packageName, className);
			}
			return null;
		}
		
		public String getProjectName() {
			return projectName;
		}
		
		public String getPackageName() {
			return packageName;
		}
		
		public String getClassName() {
			return className;
		}
		
		public String getMethodName() {
			return methodName;
		}
		
		public String toString() {
			if(type == PROJECT) {
				return projectName;
			}
			else if(type == PACKAGE) {
				return packageName;
			}
			else if(type == CLASS) {
				return className;
			}
			else if(type == METHOD) {
				return methodName;
			}
			return null;
		}
		
		public int getType() {
			return type;
		}
	}

	@Focus
	public void setFocus() {
		treeViewer.getControl().setFocus();

	}
}
