package greentea;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


public class TreeViewer {
	private org.eclipse.jface.viewers.TreeViewer viewer;
	private IWorkspace workSpace;

	public TreeViewer(Composite parent) {
		workSpace = ResourcesPlugin.getWorkspace();
		
		FilteredTree filteredTree = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FILL, new GTPatternFilter(), true);
		viewer = filteredTree.getViewer();		

		//viewer = new org.eclipse.jface.viewers.TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FILL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.getTree().setHeaderVisible(true);
		viewer.setInput(ProjectAnalyser.getProjectNames());


		TreeViewerColumn mainColumn = new TreeViewerColumn(viewer, SWT.NONE);
		mainColumn.getColumn().setText("Projects Structure");
		mainColumn.getColumn().setWidth(275);
		mainColumn.setLabelProvider(new ViewLabelProvider());

		TreeViewerColumn metric1Column = new TreeViewerColumn(viewer, SWT.NONE);
		metric1Column.getColumn().setText("Line of Code");
		metric1Column.getColumn().setWidth(150);
		metric1Column.getColumn().setAlignment(SWT.RIGHT);
		metric1Column.setLabelProvider(new MetricProvider(1));

		TreeViewerColumn metric2Column = new TreeViewerColumn(viewer, SWT.NONE);
		metric2Column.getColumn().setText("Halstead Volume");
		metric2Column.getColumn().setWidth(150);
		metric2Column.getColumn().setAlignment(SWT.RIGHT);
		metric2Column.setLabelProvider(new MetricProvider(2));

		TreeViewerColumn metric3Column = new TreeViewerColumn(viewer, SWT.NONE);
		metric3Column.getColumn().setText("Cyclomatic Complexity");
		metric3Column.getColumn().setWidth(150);
		metric3Column.getColumn().setAlignment(SWT.RIGHT);
		metric3Column.setLabelProvider(new MetricProvider(3));

		TreeViewerColumn metric4Column = new TreeViewerColumn(viewer, SWT.NONE);
		metric4Column.getColumn().setText("Martin's Coupling Metric");
		metric4Column.getColumn().setWidth(150);
		metric4Column.getColumn().setAlignment(SWT.RIGHT);
		metric4Column.setLabelProvider(new MetricProvider(4));

		TreeViewerColumn metric5Column = new TreeViewerColumn(viewer, SWT.NONE);
		metric5Column.getColumn().setText("Dhama's Coupling Metric");
		metric5Column.getColumn().setWidth(150);
		metric5Column.getColumn().setAlignment(SWT.RIGHT);
		metric5Column.setLabelProvider(new MetricProvider(5));

		TreeViewerColumn metric6Column = new TreeViewerColumn(viewer, SWT.NONE);
		metric6Column.getColumn().setText("Maintainability Index");
		metric6Column.getColumn().setWidth(150);
		metric6Column.getColumn().setAlignment(SWT.RIGHT);
		metric6Column.setLabelProvider(new MetricProvider(6));
		
		TreeViewerColumn metric7Column = new TreeViewerColumn(viewer, SWT.NONE);
		metric7Column.getColumn().setText("Abstractness");
		metric7Column.getColumn().setWidth(100);
		metric7Column.getColumn().setAlignment(SWT.RIGHT);
		metric7Column.setLabelProvider(new MetricProvider(7));
	}

	public org.eclipse.jface.viewers.TreeViewer getViewer() {
		return viewer;
	}

	class ViewContentProvider implements ITreeContentProvider {
		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if(inputElement instanceof String[]) {
				String projectNames[] = (String[])inputElement;
				List<GTPath> list = new ArrayList<GTPath>();
				for(String projectName : projectNames) {
					list.add(new GTPath(projectName));
				}
				return list.toArray(new GTPath[0]);
			}
			else {
				return null;
			}
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if(parentElement instanceof GTPath) {
				GTPath path = (GTPath)parentElement;
				List<GTPath> list = new ArrayList<GTPath>();
				if(path.getType() == GTPath.PROJECT) {
					String projectName = path.getProjectName();
					for(String packageName : ProjectAnalyser.getPackageNames(projectName)) {
						list.add(new GTPath(path, packageName));
					}
					return list.toArray(new GTPath[0]);
				}
				else if (path.getType() == GTPath.PACKAGE) {
					String projectName = path.getProjectName();
					String packageName = path.getPackageName();
					for(String className : ProjectAnalyser.getClassNames(projectName, packageName)) {
						list.add(new GTPath(path, className));
					}
					return list.toArray(new GTPath[0]);
				}
				else if (path.getType() == GTPath.CLASS) {
					String projectName = path.getProjectName();
					String packageName = path.getPackageName();
					String className = path.getClassName();
					for(String methodName : ProjectAnalyser.getMethodNames(projectName, packageName, className)) {
						list.add(new GTPath(path, methodName));
					}
					return list.toArray(new GTPath[0]);
				}
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof GTPath) {
				return ((GTPath) element).getParent();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof GTPath) {
				GTPath path = (GTPath)element;
				if(path.getType() == GTPath.PROJECT) {
					if(ProjectAnalyser.getPackageNames(path.getProjectName()).length > 0) return true;
					return false;
				}
				else if (path.getType() == GTPath.PACKAGE) {
					if(ProjectAnalyser.getClassNames(path.getProjectName(), path.getPackageName()).length > 0)return true;
					return false;
				}
				else if (path.getType() == GTPath.CLASS) {
					if(ProjectAnalyser.getMethodNames(path.getProjectName(), path.getPackageName(), path.getClassName()).length > 0)return true;
					return false;
				}
				return false;
			}
			return false;
		}
	}


	//TODO
	class ViewLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {
		private ImageDescriptor img1;
		private ImageDescriptor img2;
		private ImageDescriptor img3;
		private ImageDescriptor img4;
		private ResourceManager resourceManager;

		public ViewLabelProvider() {
			String path1, path2, path3, path4;
			Bundle bundle = FrameworkUtil.getBundle(ViewLabelProvider.class);
			path1 = "res/icons/project.png";
			path2 = "res/icons/project.png";
			path3 = "res/icons/Red.png";
			path4 = "res/icons/green.png";
			
			URL url = FileLocator.find(bundle, new Path(path1), null);
			img1 = ImageDescriptor.createFromURL(url);
			url = FileLocator.find(bundle, new Path(path2), null);
			img2 = ImageDescriptor.createFromURL(url);
			url = FileLocator.find(bundle, new Path(path3), null);
			img3 = ImageDescriptor.createFromURL(url);
			url = FileLocator.find(bundle, new Path(path4), null);
			img4 = ImageDescriptor.createFromURL(url);
		}

		@Override
		public StyledString getStyledText(Object element) {
			return new StyledString(getText(element));
		}

		@Override
		public String getText(Object element) {
			String name = "";

			if(element instanceof GTPath) {
				GTPath path = (GTPath)element;
				name = path.toString();
			}

			return name;
		}

		@Override
		public Image getImage(Object element) {
			
			if(element instanceof GTPath) {
				GTPath tmp = (GTPath)element;
				if(tmp.getType() == GTPath.PROJECT) {
					return getResourceManager().createImage(img1);
				}
				else if(tmp.getType() == GTPath.PACKAGE) {
					return getResourceManager().createImage(img2);
				}
				else if(tmp.getType() == GTPath.CLASS) {
					return getResourceManager().createImage(img3);
				}
				else if(tmp.getType() == GTPath.METHOD) {
					return getResourceManager().createImage(img4);
				}
			}
			
			
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
	class MetricProvider extends ColumnLabelProvider implements IStyledLabelProvider {
		private ImageDescriptor directoryImage;
		private ResourceManager resourceManager;

		private final int index;

		public MetricProvider(int index) {
			this.index = index;
		}

		@Override
		public String getText(Object element) {
			if(element instanceof GTPath) {
				GTPath path = (GTPath) element;
				String projectName, packageName, className, methodName, result = null;
				projectName = path.getProjectName();
				packageName = path.getPackageName();
				className = path.getClassName();
				methodName = path.getMethodName();

				//IMethod method = ProjectAnalyser.getIMethod(projectName, packageName, className, methodName);

				switch(index) {
				case 1:
					if(path.getType() == GTPath.METHOD) {
						IMethod method = ProjectAnalyser.getIMethod(projectName, packageName, className, methodName);
						result = String.valueOf(Metric.measureLOC(method));
					}
					else if(path.getType() == GTPath.CLASS) {
						result = String.valueOf(Metric.measureLOC(projectName, packageName, className));
					}
					break;
				case 2:
					if(path.getType() == GTPath.METHOD)
						result = String.valueOf(Metric.measureHalstead(projectName, packageName, className, methodName));
					break;
				case 3:
					if(path.getType() == GTPath.METHOD)
						result = String.valueOf(Metric.measureCyclomatic(projectName, packageName, className, methodName));
					break;
				case 4:
					if(path.getType() == GTPath.PACKAGE)
						result = String.valueOf(Metric.measureMartinInstability(projectName, packageName));
					break;
				case 5:
					if(path.getType() == GTPath.METHOD)
						result = String.valueOf(Metric.measureDhama(projectName, packageName, className, methodName));
					break;
				case 6:
					if(path.getType() == GTPath.METHOD) {
						IMethod method = ProjectAnalyser.getIMethod(projectName, packageName, className, methodName);
						result = String.valueOf(Metric.measureMaintain(method, projectName, packageName, className, methodName));
					}
					break;

				case 7:
					if(path.getType() == GTPath.PACKAGE) {
						result = String.valueOf(Metric.mesureAbstractness(projectName, packageName));
					}
					break;
				
				case 99: //For test
					result = String.valueOf(viewer.getTree().getColumnCount());
					break;
				}				

				return result;

			}
			return "";
		}
		
		@Override
		public StyledString getStyledText(Object element) {
			return new StyledString(getText(element));
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

	class GTPatternFilter extends PatternFilter {
		protected boolean isLeafMatch(final Viewer viewer, final Object element) {
			org.eclipse.jface.viewers.TreeViewer treeViewer = (org.eclipse.jface.viewers.TreeViewer)viewer;
			int numberOfColumns = treeViewer.getTree().getColumnCount();
			boolean isMatch = false;
			for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
				ColumnLabelProvider labelProvider = (ColumnLabelProvider)treeViewer.getLabelProvider(columnIndex);
				String labelText = labelProvider.getText(element);
				isMatch |= wordMatches(labelText);
			}
			return isMatch;
		}
	}

	public void updating()
	{
		IResourceChangeListener listener = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						viewer.setInput(ProjectAnalyser.getProjectNames());
					}
				});
			}
		};
		workSpace.addResourceChangeListener(listener);
	}

	class GTPath {
		static final int PROJECT = 1;
		static final int PACKAGE = 2;
		static final int CLASS = 3;
		static final int METHOD = 4;

		private final int type;

		private String projectName;
		private String packageName;
		private String className;
		private String methodName;

		public GTPath(String projectName) {
			type = PROJECT;
			this.projectName = projectName;
		}

		public GTPath(String projectName, String packageName) {
			type = PACKAGE;
			this.projectName = projectName;
			this.packageName = packageName;
		}

		public GTPath(String projectName, String packageName, String className) {
			type = CLASS;
			this.projectName = projectName;
			this.packageName = packageName;
			this.className = className;
		}

		public GTPath(String projectName, String packageName, String className, String methodName) {
			type = METHOD;
			this.projectName = projectName;
			this.packageName = packageName;
			this.className = className;
			this.methodName = methodName;
		}

		public GTPath(GTPath oldPath, String str) {
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

		public GTPath getParent() {
			if (type == PACKAGE) {
				return new GTPath(projectName);
			}
			else if (type == CLASS) {
				return new GTPath(projectName, packageName);
			}
			else if (type == METHOD) {
				return new GTPath(projectName, packageName, className);
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
			return "";
		}

		public int getType() {
			return type;
		}
	}

}

