/* copyright
*
* EmpatPetak - A simple record-jar-based text editor
* (C) 2020 Keian Rao
*
* This program is free software: you can redistribute it and/or 
* modify it under the terms of the GNU General Public License as
* published by the Free Software Foundation, either version 3 of 
* the License, or (at * your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see
* <https://www.gnu.org/licenses/>.
*
copyright */
/*
* The 'without warranty' part applies a bit particularly here.
* I coded this in a plain ball-of-mud manner, without any 
* test suites nor particular care in checking the code.
* I believe this program is safe but, please avoid opening 
* (in this program) any file not created from this program.
*
* I apologise for not taking the time to make sure it's proper.
* This is a simple utility for personal use, much like a script.
*/

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class EmpatPetak implements ActionListener {

private JFrame mainframe;
private JPanel mainpanel;

private JMenuBar menubar;
private JMenu fileMenu;
private JMenuItem fileMenuQuit;
private JMenuItem fileMenuLoad;
private JMenuItem fileMenuSave;

private JTextArea textAreaTL;
private JTextArea textAreaTR;
private JTextArea textAreaBL;
private JTextArea textAreaBR;
private JScrollPane textAreaTLPane;
private JScrollPane textAreaTRPane;
private JScrollPane textAreaBLPane;
private JScrollPane textAreaBRPane;
private Border textAreaBorder;
private Font textAreaFont;

private JFileChooser fileChooser;


//	\\	//	\\	//	\\	//	\\


private static final int TEXT_AREA_GAP = 5;
private static final int MAINPANEL_INSET = 5;
private static final Color BACKGROUND_COLOUR = new Color(225, 219, 216);
private static final Color FOREGROUND_COLOUR = new Color(178, 77, 122);


//	\\	//	\\	//	\\	//	\\


public static void main(String... args) {
	EmpatPetak instance = new EmpatPetak();
	instance.mainframe.setVisible(true);
}


//	\\	//	\\	//	\\	//	\\


private EmpatPetak() {
	mainframe = new JFrame("Empat Petak");
	
	JPanel mainpanel = new JPanel();
	mainpanel.setLayout(
		new GridLayout(
			2, 2, 
			TEXT_AREA_GAP, TEXT_AREA_GAP
		)
	);
	mainpanel.setBorder(
		BorderFactory.createEmptyBorder(
			MAINPANEL_INSET, 
			MAINPANEL_INSET, 
			MAINPANEL_INSET, 
			MAINPANEL_INSET
		)
	);
	mainpanel.setBackground(BACKGROUND_COLOUR);
	mainframe.setContentPane(mainpanel);
	
	initMenubar();
	mainframe.setJMenuBar(menubar);
	
	initTextAreas();
	mainframe.add(textAreaTLPane);
	mainframe.add(textAreaTRPane);
	mainframe.add(textAreaBLPane);
	mainframe.add(textAreaBRPane);
	
	initBackend();
	
	mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	mainframe.setSize(800, 600);
}

private void initMenubar() {
	menubar = new JMenuBar();
	
	fileMenu = new JMenu("File");
	
	fileMenuLoad = new JMenuItem("Open", KeyEvent.VK_O);
	fileMenuLoad.addActionListener(this);
	fileMenu.add(fileMenuLoad);
	
	fileMenuSave = new JMenuItem("Save", KeyEvent.VK_S);
	fileMenuSave.addActionListener(this);
	fileMenu.add(fileMenuSave);
	
	fileMenuQuit = new JMenuItem("Quit", KeyEvent.VK_Q);
	fileMenuQuit.addActionListener(this);
	fileMenu.add(fileMenuQuit);

	menubar.add(fileMenu);
}

private void initTextAreas() {
	textAreaTL = new JTextArea();
	textAreaTR = new JTextArea();
	textAreaBL = new JTextArea();
	textAreaBR = new JTextArea();
	textAreaTLPane = new JScrollPane(textAreaTL);
	textAreaTRPane = new JScrollPane(textAreaTR);
	textAreaBLPane = new JScrollPane(textAreaBL);
	textAreaBRPane = new JScrollPane(textAreaBR);

	textAreaBorder = BorderFactory.createLoweredSoftBevelBorder();
	textAreaTL.setBorder(textAreaBorder);
	textAreaTR.setBorder(textAreaBorder);
	textAreaBL.setBorder(textAreaBorder);
	textAreaBR.setBorder(textAreaBorder);
	textAreaTLPane.setBorder(textAreaBorder);
	textAreaTRPane.setBorder(textAreaBorder);
	textAreaBLPane.setBorder(textAreaBorder);
	textAreaBRPane.setBorder(textAreaBorder);
	
	textAreaFont = new Font("Monospaced", Font.BOLD, 12);
	textAreaTL.setFont(textAreaFont);
	textAreaTR.setFont(textAreaFont);
	textAreaBL.setFont(textAreaFont);
	textAreaBR.setFont(textAreaFont);
	
	textAreaTL.setForeground(FOREGROUND_COLOUR);
	textAreaTR.setForeground(FOREGROUND_COLOUR);
	textAreaBL.setForeground(FOREGROUND_COLOUR);
	textAreaBR.setForeground(FOREGROUND_COLOUR);
		
	textAreaTL.setLineWrap(true);
	textAreaTR.setLineWrap(true);
	textAreaBL.setLineWrap(true);
	textAreaBR.setLineWrap(true);
	
	textAreaTL.setOpaque(false);
	textAreaTR.setOpaque(false);
	textAreaBL.setOpaque(false);
	textAreaBR.setOpaque(false);
	textAreaTLPane.setOpaque(false);
	textAreaTRPane.setOpaque(false);
	textAreaBLPane.setOpaque(false);
	textAreaBRPane.setOpaque(false);
	textAreaTLPane.getViewport().setOpaque(false);
	textAreaTRPane.getViewport().setOpaque(false);
	textAreaBLPane.getViewport().setOpaque(false);
	textAreaBRPane.getViewport().setOpaque(false);
}

private void initBackend() {
	fileChooser = new JFileChooser();
}


//	\\	//	\\	//	\\	//	\\


public void actionPerformed(ActionEvent e) {
	if (e.getSource() == fileMenuLoad) try {
		int returnCode = fileChooser.showOpenDialog(mainframe);
		if (returnCode == JFileChooser.APPROVE_OPTION) {
			open(fileChooser.getSelectedFile());
		}
	}
	catch (IOException eIo) {
		showException(eIo);
	}
	
	else if (e.getSource() == fileMenuSave) try {
		int returnCode = fileChooser.showSaveDialog(mainframe);
		if (returnCode == JFileChooser.APPROVE_OPTION) {
			save(fileChooser.getSelectedFile());
		}
	}
	catch (IOException eIo) {
		showException(eIo);
	}
	
	else if (e.getSource() == fileMenuQuit) {
		System.exit(0);
	}
}

private void showException(Exception e) {
	JOptionPane.showMessageDialog(mainframe, e.getMessage());
}


private void open(File file) throws IOException {
	BufferedReader reader = new BufferedReader(new FileReader(file));
	
	textAreaTL.setText("");
	textAreaTR.setText("");
	textAreaBL.setText("");
	textAreaBR.setText("");
	
	JTextArea section = textAreaTL;
	String line;
	boolean firstLineOfSection = true;
	
	while ( (line = reader.readLine()) != null )
	{
		if (line.trim().equals("%%")) {
			if (section == textAreaTL) section = textAreaTR;
			else if (section == textAreaTR) section = textAreaBL;
			else if (section == textAreaBL) section = textAreaBR;
			else break;

			firstLineOfSection = true;
			continue; 
		}
		
		if (firstLineOfSection) {
			firstLineOfSection = false;
		}
		else {
			section.append("\n");
		}
		section.append(line);
	}
	
	reader.close();
}

private void save(File file) throws IOException {
	file.createNewFile();
	
	FileWriter writer = new FileWriter(file);
	
	String lineSeparator = System.lineSeparator();
	String sectionSeparator = lineSeparator + "%%" + lineSeparator;
	writer.write(textAreaTL.getText());
	writer.write(sectionSeparator);
	writer.write(textAreaTR.getText());
	writer.write(sectionSeparator);
	writer.write(textAreaBL.getText());
	writer.write(sectionSeparator);
	writer.write(textAreaBR.getText());
	writer.write(lineSeparator);
	
	writer.close();
}

}
