import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server1 implements ActionListener {

    JTextField text;
    JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;

    Server1() {
        f.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/arrow.jpg"));
        JLabel back = new JLabel(new ImageIcon(i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT)));
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/gaitonde.jpeg"));
        JLabel profile = new JLabel(new ImageIcon(i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        JLabel video = new JLabel(new ImageIcon(i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT)));
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        JLabel phone = new JLabel(new ImageIcon(i10.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT)));
        phone.setBounds(360, 20, 35, 30);
        p1.add(phone);

        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        JLabel threeicon = new JLabel(new ImageIcon(i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT)));
        threeicon.setBounds(420, 20, 10, 25);
        p1.add(threeicon);

        JLabel name = new JLabel("Itachi Uchiha");
        name.setBounds(110, 15, 100, 20);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        p1.add(status);

        // Wallpaper background
        JLabel bg = new JLabel(new ImageIcon(ClassLoader.getSystemResource("icons/wallpaper1.jpg")));
        bg.setBounds(0, 70, 450, 630);
        f.add(bg);

        a1 = new JPanel();
        a1.setLayout(new BorderLayout());
        a1.setOpaque(false); // Transparent for wallpaper
        JScrollPane scrollPane = new JScrollPane(vertical);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        a1.add(scrollPane, BorderLayout.CENTER);
        a1.setBounds(5, 75, 440, 570);
        f.add(a1);

        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(37, 211, 102));
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        f.add(send);

        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (dout == null) {
                JOptionPane.showMessageDialog(f, "No client connected yet!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String out = text.getText();
            JPanel p2 = formatLabel(out);

            JPanel right = new JPanel(new BorderLayout());
            right.setOpaque(false); // Transparent
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            dout.writeUTF(out);
            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
            vertical.revalidate();
            vertical.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false); // Transparent wrapper

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel(sdf.format(cal.getTime()));
        time.setForeground(Color.DARK_GRAY);

        panel.add(time);
        panel.add(output);

        return panel;
    }

    public static void main(String[] args) {
        new Server1();
        try {
            ServerSocket skt = new ServerSocket(6001);
            while (true) {
                Socket s = skt.accept();
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());

                while (true) {
                    String msg = din.readUTF();
                    JPanel panel = formatLabel(msg);

                    JPanel left = new JPanel(new BorderLayout());
                    left.setOpaque(false); // Transparent
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    vertical.add(Box.createVerticalStrut(15));
                    f.validate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
