package greentea;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import java.util.ArrayList;
import java.util.List;

public class GreenTea {
	private TreeViewer treeViewer;
	private org.eclipse.jface.viewers.TreeViewer viewer;

	/*
	 * composite parent -> separate to some area and only left area used for treeviewrcol.
	 * 	Will be moved to treeViewer
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new FillLayout());
        comp.setLayout(new GridLayout(2, false));
        comp.setSize(100, 80);
		Button tutorialBtn = new Button(comp, SWT.PUSH);
		tutorialBtn.setText("Tutorial");

		treeViewer = new TreeViewer(parent);
		viewer = treeViewer.getViewer();

	    viewer.addDoubleClickListener(new IDoubleClickListener() {
        	@Override
            public void doubleClick(DoubleClickEvent event) {
        		org.eclipse.jface.viewers.TreeViewer viewer = (org.eclipse.jface.viewers.TreeViewer) event.getViewer();
                IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
                Object selectedNode = thisSelection.getFirstElement();
                viewer.setExpandedState(selectedNode,!viewer.getExpandedState(selectedNode));
            }
        });
	    
	    
	    tutorialBtn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Bundle bundle = Platform.getBundle("GreenTea");
				try {
					URL eclipseURL = FileLocator.find(bundle, new Path("res/tutorial.md"), null);
					File bbb = FileLocator.getBundleFile(bundle);
					URL fileURL = FileLocator.toFileURL(eclipseURL);
					File file = new File(FileLocator.resolve(fileURL).toURI());

					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IDE.openEditor(page, file.toURI(), "org.eclipse.mylyn.wikitext.ui.editor.markupEditor", true);
				} catch (IOException | URISyntaxException | PartInitException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
	    	
	    });
	    
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();

	}
}
