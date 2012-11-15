package com.change_vision.astah.xmi.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import com.change_vision.astah.xmi.Messages;
import com.change_vision.astah.xmi.XmiImporter;

public class ImportXmiDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -2188599889594742793L;

	private static ImportXmiDialog instance;
	private JTextField fromText;
	private static String directory = System.getProperty("user.home");
	
	private ImportXmiDialog(JFrame owner) {
		super(owner, true);
		
        JPanel basePanel = createBasePanel();
        JPanel okPanel = createOkPanel();

        Container contentPane = getContentPane();
        contentPane.add(basePanel, BorderLayout.NORTH);
        contentPane.add(okPanel, BorderLayout.SOUTH);
        
        doLayout();
        setTitle(Messages.getMessage("dialog.title"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setLocation();
	}

	private JPanel createOkPanel() {
        JPanel okPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton(Messages.getMessage("button.text.import"));
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        JButton cancelButton = new JButton(Messages.getMessage("button.text.close"));
        cancelButton.setActionCommand("CANCEL");
        cancelButton.addActionListener(this);
        okPanel.add(okButton);
        okPanel.add(cancelButton);

        JPanel okBasePanel = new JPanel(new BorderLayout());
        okBasePanel.add(new JSeparator(), BorderLayout.NORTH);
        okBasePanel.add(okPanel, BorderLayout.SOUTH);
        return okBasePanel;
    }

	private JPanel createBasePanel() {
        
        fromText = new JTextField(40);
        JButton fromButton = new JButton(Messages.getMessage("button.text.select"));
        fromButton.setActionCommand("Select");
        fromButton.addActionListener(this);
        JPanel basePanel = new JPanel(new FlowLayout(1));
        basePanel.add(fromText);
        basePanel.add(fromButton);
        return basePanel;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("Select")) {
			JFileChooser chooser = new JFileChooser(directory);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "XMI2.1 Files (.xmi, .uml)";
				}
				
				@Override
				public boolean accept(File f) {
					return f.isDirectory() || f.getName().endsWith(".xmi") || f.getName().endsWith(".uml");
				}
			});
			if (chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				if (selectedFile != null) {
					fromText.setText(selectedFile.getAbsolutePath());
					directory = selectedFile.getAbsolutePath();
				}
			}
		} else if (cmd.equals("OK")) {
			String from = fromText.getText();
			if (!(from.endsWith(".xmi") || from.endsWith(".uml")) || !new File(from).exists()) {
				MessageView.showErrorMessage(getParent(), Messages.getMessage("warning_message.incorrect_xmi_file"));
			} else {
			    Cursor currentCursor = getCursor();
			    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				try {
                    XmiImporter.doImport(from);
                } finally{
                    setCursor(currentCursor);
                }
				dispose();
			}
		} else if (cmd.equals("CANCEL")) {
			dispose();
		}
	}

	private void setLocation() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width-frameSize.width) / 2, (screenSize.height-frameSize.height) / 2);
	}

	public static ImportXmiDialog getInstance(JFrame frame)  throws Throwable {
		if (instance == null) {
			instance = new ImportXmiDialog(frame);
		}
		return instance;
	}
	
	public static void main(String[] args) {
        ImportXmiDialog dialog = new ImportXmiDialog(null);
        dialog.setVisible(true);
    }
}
