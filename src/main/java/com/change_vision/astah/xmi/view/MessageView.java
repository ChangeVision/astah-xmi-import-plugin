package com.change_vision.astah.xmi.view;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.change_vision.astah.xmi.Messages;

public class MessageView {


	public static void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message, Messages.getMessage("dialog.title.information"), JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showErrorMessage(Component parentComponent, String message) {
		JOptionPane.showMessageDialog(parentComponent, message, Messages.getMessage("dialog.title.error"), JOptionPane.ERROR_MESSAGE);
	}

	public static void showInformationDialog(Component parentComponent, String message) {
		JOptionPane.showMessageDialog(parentComponent, message, Messages.getMessage("dialog.title.infomation"), JOptionPane.INFORMATION_MESSAGE);
	}
}