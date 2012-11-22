package com.change_vision.astah.xmi;

import java.io.File;
import java.net.URI;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.xmi.internal.convert.XmiToAstah;
import com.change_vision.astah.xmi.view.MessageView;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

public class XmiImporter {

	private static final Logger logger = LoggerFactory.getLogger(XmiImporter.class);

	public static void doImport(String from) {
		go(from);
	}

	private static void go(final String from) {
	    if(SwingUtilities.isEventDispatchThread()){
            startImport(from);	        
	    }else{
	        final Thread worker = new Thread() {
	            @Override
	            public void run() {
	                startImport(from);
	            }
	        };
	        SwingUtilities.invokeLater(worker);
	    }
	}

	private static void startImport(String from) {
		try {
			ProjectAccessor pa = ProjectAccessorFactory.getProjectAccessor();
			pa.create();
			
			URI uri = new File(from).toURI();
			XmiToAstah converter = new XmiToAstah(uri.toString());
			converter.convert();
			
			MessageView.showMessage(Messages.getMessage("message.import.successfully"));

		} catch (LicenseNotFoundException e) {
			logger.error(e.getMessage(), e);
			MessageView.showErrorMessage(null, e.getMessage());
		} catch (ProjectLockedException e) {
			logger.error(e.getMessage(), e);
			MessageView.showErrorMessage(null, e.getMessage());
		} catch(InvalidEditingException e) {
			logger.error(e.getMessage(), e);
			MessageView.showErrorMessage(null, e.getMessage());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			MessageView.showErrorMessage(null, e.getMessage());
		}
	}
}
