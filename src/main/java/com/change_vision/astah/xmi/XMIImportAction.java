package com.change_vision.astah.xmi;


import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.xmi.view.ImportXmiDialog;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.exception.ProjectLockedException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class XMIImportAction implements IPluginActionDelegate {

	private static final Logger logger = LoggerFactory.getLogger(XMIImportAction.class);
	
	public Object run(IWindow window) throws UnExpectedException {
		try {
//			ProjectAccessor projectAccessor = ProjectAccessorFactory.getProjectAccessor();
//			if ("no_title".equals(projectAccessor.getProjectPath())) {
//				int result = JOptionPane.showConfirmDialog(window.getParent(),
//						Messages.getMessage("warning_message.ask_save"),
//						Messages.getMessage("dialog.title.confirm"), JOptionPane.YES_NO_OPTION);
//				switch (result) {
//					case JOptionPane.YES_OPTION:
//						try {
//							projectAccessor.save();
//						} catch (IOException e) {
//							// 保存の取り消し
//							return null;
//						}
//						break;
//					case JOptionPane.NO_OPTION:
//						return null;
//					default:
//						return null;
//				}
//			}

			ImportXmiDialog instance = ImportXmiDialog.getInstance((JFrame) window.getParent());
			instance.setVisible(true);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (ProjectNotFoundException e) {
			JOptionPane.showMessageDialog(window.getParent(),
					Messages.getMessage("message.project_not_open"), Messages.getMessage("dialog.title.warning"),
					JOptionPane.WARNING_MESSAGE);
		} catch (LicenseNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (ProjectLockedException e) {
			logger.error(e.getMessage(), e);
		} catch (Throwable e) {
			JOptionPane.showMessageDialog(window.getParent(),
					Messages.getMessage("message.unknown_error"), Messages.getMessage("dialog.title.error"),
					JOptionPane.ERROR_MESSAGE);
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}

