package greentea;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


public class Real_update {
	
	public class componentModel{
		public componentModel parent = null;
		public List<componentModel> children = new ArrayList<>();
		@Override
		public String toString() {
			return "ComponentModel";
		}
	}
	public class projectComp extends componentModel{
		public IProject project;
		public projectComp(IProject p, componentModel parent) {
			// TODO Auto-generated constructor stub
			project = p;
			this.parent = parent;
		}
		@Override
		public String toString() {
			return project.getName();
		}
	}
	public class packageComp extends componentModel{
		public IPackageFragment pack;
		public packageComp(IPackageFragment pf, componentModel parent) {
			// TODO Auto-generated constructor stub
			pack = pf;
			this.parent = parent;
		}
		@Override
		public String toString() {
			return pack.getElementName();
		}
	}
	public class sourceComp extends componentModel{
		public ICompilationUnit classFile;
		public sourceComp(ICompilationUnit cu, componentModel parent) {
			// TODO Auto-generated constructor stub
			classFile = cu;
			this.parent = parent;
		}
		@Override
		public String toString() {
			return classFile.getElementName();
		}
	}
	public class fieldComp extends componentModel{
		public IField field;
		public fieldComp(IField f, componentModel parent) {
			// TODO Auto-generated constructor stub
			field = f;
			this.parent = parent;
		}
		@Override
		public String toString() {
			return field.getElementName();
		}
	}
	public class methodComp extends componentModel{
		public IMethod method;
		public methodComp(IMethod m, componentModel parent) {
			// TODO Auto-generated constructor stub
			method = m;
			this.parent = parent;
		}
		@Override
		public String toString() {
			return method.getElementName();
		}
	}
	private class contentProvider implements ITreeContentProvider{
		@Override
		public Object[] getElements(Object inputElement) {
			return ((componentModel) inputElement).children.toArray();
		}
		@Override
		public Object[] getChildren(Object parentElement){
			return ((componentModel) parentElement).children.toArray();
		}
		@Override
		public Object getParent(Object element) {
			if(element instanceof componentModel)
				return ((componentModel) element).parent;
			else
				return null;
		}
		@Override
		public boolean hasChildren(Object element) {
			if(element instanceof componentModel)
				return !((componentModel) element).children.isEmpty();
			else
				return false;
		}
		@Override
		public void dispose() {
			
		}
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
		}
	}
	private class labelProvider extends LabelProvider{
		@Override
		public Image getImage(Object element) {
			String path = null;
			Bundle bundle = FrameworkUtil.getBundle(labelProvider.class);
			
			if(element instanceof projectComp)
				path = "icons/folder.png";
			else if(element instanceof packageComp)
				path = "icons/package.png";
			else if(element instanceof sourceComp)
				path = "icons/file.png";
			else if(element instanceof fieldComp)
				path = "icons/bullet_pink.png";
			else if(element instanceof methodComp)
				path = "icons/bullet_green.png";
			
			URL url = FileLocator.find(bundle, new Path(path), null);
			ImageDescriptor preImg = ImageDescriptor.createFromURL(url);
			return preImg.createImage();
		}
	}
	private IWorkspace workSpace;
	private IProject[] projects;
	private componentModel treeComponent;
	private List<projectComp> projectComps;
	private List<packageComp> packageComps;
	private List<sourceComp> sourceComps;
	private List<componentModel> terminalComps;
	private TreeViewer treeV;
	
	public Real_update() {
		// TODO Auto-generated constructor stub
		workSpace = ResourcesPlugin.getWorkspace();
		
	}
	public void update() {
		projects = workSpace.getRoot().getProjects();
		treeComponent = new componentModel();
		projectComps = new ArrayList<> ();
		packageComps = new ArrayList<>();
		sourceComps = new ArrayList<>();
		terminalComps = new ArrayList<>();
		for(IProject project : projects) {
			projectComps.add(new projectComp(project, null));
		}
		for(projectComp projectc : projectComps) {
			treeComponent.children.add(projectc);
			try {
				if(projectc.project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
					 IJavaProject javaP = JavaCore.create(projectc.project);
					 IPackageFragment[] pf = javaP.getPackageFragments();
					 for(int i = 0; i < pf.length; i++) {
						 if(pf[i].getKind() == IPackageFragmentRoot.K_SOURCE &&
							pf[i].getElementName() != "" &&
							pf[i].getCompilationUnits().length != 0) {
							 packageComp packagec = new packageComp(pf[i], projectc);
							 packageComps.add(packagec);
							 projectc.children.add(packagec);
						 }
					 }
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(packageComp packagec : packageComps) {
			try {
				ICompilationUnit[] sources = packagec.pack.getCompilationUnits();
				for(ICompilationUnit source : sources) {
					sourceComp sourcec = new sourceComp(source, packagec);
					sourceComps.add(sourcec);
					packagec.children.add(sourcec);
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(sourceComp sourcec : sourceComps) {
			try {
				IType[] types = sourcec.classFile.getAllTypes();
				for(IType type : types) {
					IMethod[] methods = type.getMethods();
					IField[] fields = type.getFields();
					for(IMethod method : methods) {
						methodComp methodc = new methodComp(method, sourcec);
						sourcec.children.add(methodc);
						terminalComps.add(methodc);
					}
					for(IField field : fields) {
						fieldComp fieldc = new fieldComp(field, sourcec);
						sourcec.children.add(fieldc);
						terminalComps.add(fieldc);
					}
				}	
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void treeUpdate(componentModel tc) {
        treeV.setInput(treeComponent);
        return;
	}
	@PostConstruct
	public void createPartControl(Composite parent) {
		IResourceChangeListener listener = new IResourceChangeListener() {
		      public void resourceChanged(IResourceChangeEvent event) {
		    	 update();
		    	 Display.getDefault().asyncExec(new Runnable() {
		    		 public void run() {
				         treeUpdate(treeComponent);
		    		 }
		    	 });
		      }
		   };
		update();
		treeV = new TreeViewer(parent, SWT.H_SCROLL|SWT.V_SCROLL);
		treeV.setContentProvider(new contentProvider());
		treeV.setLabelProvider(new labelProvider());
		treeV.setInput(treeComponent);
		treeV.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				IStructuredSelection target = (IStructuredSelection) event.getSelection();
				componentModel targetObject = (componentModel) target.getFirstElement();
				if(targetObject == null)
					return;
				if(targetObject instanceof fieldComp || 
				   targetObject instanceof methodComp) {
					ICompilationUnit icu = ((sourceComp)targetObject.parent).classFile;
					ISourceRange range = null;
					if(targetObject instanceof fieldComp) {
						try {
							range = ((fieldComp)targetObject).field.getSourceRange();
						} catch (JavaModelException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(targetObject instanceof methodComp) {
						try {
							range = ((methodComp)targetObject).method.getSourceRange();
						} catch (JavaModelException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					IFile sourceFile = null;
					try {
						IResource sourceRe = icu.getUnderlyingResource();
						if(sourceRe.getType() == IResource.FILE) {
							sourceFile = (IFile) sourceRe;
						}
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(sourceFile != null && 
							range != null) {
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						ITextEditor textEdit = null;
						try {
							textEdit = (ITextEditor)IDE.openEditor(page, sourceFile);
						} catch (PartInitException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(textEdit != null)
							textEdit.selectAndReveal(range.getOffset(), range.getLength());
					}
				}
				else {
					TreeViewer tv = (TreeViewer)event.getSource();
					if(tv.getExpandedState(targetObject)) {
						tv.collapseToLevel(targetObject, AbstractTreeViewer.ALL_LEVELS);
					}
					else {
						tv.expandToLevel(targetObject, 1);
					}
				}
			}
		});
		workSpace.addResourceChangeListener(listener);
		
	}

	
}
