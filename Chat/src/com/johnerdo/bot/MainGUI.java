package com.johnerdo.bot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.johnerdo.commands.ImageIconURL;

public class MainGUI {

    String      appName     = "DarcelBot";
    MainGUI     mainGUI;
    JFrame      newFrame    = new JFrame(appName);
    JButton     sendMessage;
    JTextField  messageBox;
    static JTextPane    chatBox;
    JTextField  usernameChooser;
    JFrame      preFrame;
    static DarcelBot bot;
    static boolean greenBackground = false;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MainGUI mainGUI = new MainGUI();
                mainGUI.preDisplay();
            }
        });
    }

    public void preDisplay() {
        newFrame.setVisible(false);
        preFrame = new JFrame(appName);
        usernameChooser = new JTextField(15);
        JLabel chooseUsernameLabel = new JLabel("Pick a channel to join:");
        JButton enterServer = new JButton("Enter Chat Server");
        preFrame.getRootPane().setDefaultButton(enterServer);
        enterServer.addActionListener(new enterServerButtonListener());
        JPanel prePanel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 0, 10);
        // preRight.weightx = 2.0;
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        prePanel.add(chooseUsernameLabel, preLeft);
        prePanel.add(usernameChooser, preRight);
        preFrame.add(BorderLayout.CENTER, prePanel);
        preFrame.add(BorderLayout.SOUTH, enterServer);
        preFrame.setSize(300, 300);
        preFrame.setVisible(true);

    }

    public void display() {
    	ImageIconURL.setupImgToUrlMapping();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        //southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());

        chatBox = new JTextPane ();
        if(greenBackground)
        	chatBox.setBackground(Color.GREEN);
        chatBox.setEditable(true);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 12));
        EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
        chatBox.setBorder(eb);
        //chatBox.setLineWrap(true);
        DefaultCaret caret = (DefaultCaret)chatBox.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(470, 300);
        newFrame.setVisible(true);
        newFrame.getRootPane().setDefaultButton(sendMessage);
    }

    static Color color1 = Color.GRAY;
    static Color color2 = Color.white;
    static int counter=0;
    
    public static void appendToPane(JTextPane tp, String msg, Color c) throws BadLocationException
    {
    	//addIcronToJTexPane(tp);
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        /*if(counter%2==0)
        	aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Background, color1);
        else
        	aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Background, color2);*/
        
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        
        
        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
        //tp.setBackground(Color.GREEN);
        
        //Run emoticon iteration 
        for(String discription: ImageIconURL.imgToURLMapping.keySet())
        	addIcronToJTexPane(tp, sc, discription, ImageIconURL.imgToURLMapping.get(discription));
    }
    
    
    public static void addIcronToJTexPane(JTextPane tp, StyleContext sc, String discription, String imgURL) throws BadLocationException{

		StyledDocument doc = tp.getStyledDocument();
		Icon icon = null;
		try {
			icon = createImageIcon(imgURL, discription);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String msg = doc.getText(0, doc.getLength());
		int start =0;
		int i=msg.indexOf(discription);
		while(i>=0) {
            final SimpleAttributeSet attrs=new SimpleAttributeSet(
               doc.getCharacterElement(start+i).getAttributes());
            if (StyleConstants.getIcon(attrs)==null) {
                StyleConstants.setIcon(attrs, icon);
                doc.remove(start+i, discription.length());
                doc.insertString(start+i,discription, attrs);
            }
            i=msg.indexOf(discription, i+discription.length());
        }
    }
    static HashMap<String, ImageIcon> iconCache = new HashMap<String, ImageIcon>();

    /** Returns an ImageIcon, or null if the path was invalid. 
     * @throws MalformedURLException */
    protected static ImageIcon createImageIcon(String path,
                                               String description) throws MalformedURLException {
        java.net.URL imgURL = new URL(path);
        if (imgURL != null) {
        	if(!iconCache.containsKey(path))
        		iconCache.put(path, new ImageIcon(imgURL, description));
            return iconCache.get(path);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    static ImageIcon createImage() {
        BufferedImage res=new BufferedImage(17, 17, BufferedImage.TYPE_INT_ARGB);
        Graphics g=res.getGraphics();
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.yellow);
        g.fillOval(0,0,16,16);
 
        g.setColor(Color.black);
        g.drawOval(0,0,16,16);
 
        g.drawLine(4,5, 6,5);
        g.drawLine(4,6, 6,6);
 
        g.drawLine(11,5, 9,5);
        g.drawLine(11,6, 9,6);
 
        g.drawLine(4,10, 8,12);
        g.drawLine(8,12, 12,10);
        g.dispose();
 
        return new ImageIcon(res);
    }
    
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (messageBox.getText().length() < 1) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                //chatBox.append("<DarcelBot>:  " + messageBox.getText()
                //        + "\n");
            	//appendToPane(chatBox,"<DarcelBot>:  " + messageBox.getText() +"\n",Color.red);
            	try {
					DarcelBot.writeChannelStuff("darcelBot",messageBox.getText());
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                bot.sendMessage("#"+channel, messageBox.getText());
                messageBox.setText("");
                
                
            }
            messageBox.requestFocusInWindow();
        }
    }

    public static String channel = "emre801";

    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            channel = usernameChooser.getText();
            if (channel.length() < 1) {
                System.out.println("No!");
            } else {
                preFrame.setVisible(false);
                display();
                try {
                	appName += ": " +channel; 
                	newFrame.setTitle(appName);	
                	bot = new DarcelBot("#"+channel, chatBox);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }

    }
}