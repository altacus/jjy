import java.applet.Applet;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Calendar;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class JavaJJY extends Applet implements Runnable, ActionListener {
	Image img;
	Graphics gr;
	boolean isRunning = false;
	boolean isPause = true;
	Dimension thisSize;
	byte[] wave02;
	byte[] wave05;
	byte[] wave08;
	int length02;
	int length05;
	int length08;
	final int SAMPLE_RATE = 26666;
	AudioFormat audioformat;
	SourceDataLine sourcedataline = null;
	boolean[] p = new boolean[60];
	Button startBT;

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startBT)
			if (isPause) {
				startBT.setLabel("Stop");
				sourcedataline.start();
				isPause = false;
			} else {
				startBT.setLabel("Start");
				sourcedataline.stop();
				sourcedataline.flush();
				isPause = true;
			}
	}

	public void destroy() {
		sourcedataline.stop();
		sourcedataline.flush();
	}

	public void init() {
		thisSize = getSize();
		img = createImage(thisSize.width, thisSize.height);
		gr = img.getGraphics();
		gr.setFont(new Font("SansSerif", 1, 14));

		startBT = new Button();
		String str = getParameter("autostart");
		if (str == null)
			str = "no";
		if (str.equals("yes"))
			isPause = false;
		else
			isPause = true;

		add(startBT);
		setLayout(null);
		startBT.setBounds(210, 12, 70, 30);
		startBT.addActionListener(this);

		audioformat = new AudioFormat(26666.0F, 8, 1, true, true);

		Info sourceInfo = new Info(SourceDataLine.class, audioformat);
		if (!AudioSystem.isLineSupported(sourceInfo)) {
			System.out.println("Line type not supported: " + sourceInfo);
			System.exit(1);
		}
		try {
			sourcedataline = ((SourceDataLine) AudioSystem.getLine(sourceInfo));
			sourcedataline.open(audioformat, 26666);
		} catch (LineUnavailableException e) {
			System.out.println(e);
			System.exit(1);
		}

		length02 = 5333;
		length05 = 13333;
		length08 = (26666 - length02);

		wave08 = new byte[26666];
		wave05 = new byte[26666];
		wave02 = new byte[26666];

		for (int i = 0; i < wave08.length; i++) {
			if (i < length08) {
				if (i % 2 == 0)
					wave08[i] = 120;
				else
					wave08[i] = -120;
			} else {
				wave08[i] = 0;
			}
		}

		for (int i = 0; i < wave05.length; i++) {
			if (i < length05) {
				if (i % 2 == 0)
					wave05[i] = 120;
				else
					wave05[i] = -120;
			} else {
				wave05[i] = 0;
			}
		}

		for (int i = 0; i < wave02.length; i++) {
			if (i < length02) {
				if (i % 2 == 0)
					wave02[i] = 120;
				else
					wave02[i] = -120;
			} else {
				wave02[i] = 0;
			}

		}

		if (isPause) {
			startBT.setLabel("Start");
		} else {
			startBT.setLabel("Stop");
			sourcedataline.start();
		}
	}

	public void start() {
		isRunning = true;
		Thread t = new Thread(this);
		t.start();
	}

	public void stop() {
		isRunning = false;
	}

	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}

	public void update(Graphics g) {
		paint(g);
	}

	private void makeTimecode(Calendar cal) {
		for (int i = 0; i < 60; i++) {
			p[i] = true;
		}

		int i = cal.get(Calendar.MINUTE);
		int j = cal.get(Calendar.HOUR_OF_DAY);
		int k = cal.get(Calendar.DAY_OF_YEAR);
		int m = cal.get(Calendar.YEAR);
		int n = cal.get(Calendar.DAY_OF_WEEK) - 1;
		int i1 = 0;
		int i2 = 0;

		int i3 = i;

		p[0] = true;
		if (i >= 40) {
			p[1] = true;
			i -= 40;
			i2++;
		} else {
			p[1] = false;
		}
		if (i >= 20) {
			p[2] = true;
			i -= 20;
			i2++;
		} else {
			p[2] = false;
		}
		if (i >= 10) {
			p[3] = true;
			i -= 10;
			i2++;
		} else {
			p[3] = false;
		}
		p[4] = false;

		if (i >= 8) {
			p[5] = true;
			i -= 8;
			i2++;
		} else {
			p[5] = false;
		}
		if (i >= 4) {
			p[6] = true;
			i -= 4;
			i2++;
		} else {
			p[6] = false;
		}
		if (i >= 2) {
			p[7] = true;
			i -= 2;
			i2++;
		} else {
			p[7] = false;
		}
		if (i >= 1) {
			p[8] = true;
			i2++;
		} else {
			p[8] = false;
		}
		p[9] = true;
		p[10] = false;
		p[11] = false;

		if (j >= 20) {
			p[12] = true;
			j -= 20;
			i1++;
		} else {
			p[12] = false;
		}
		if (j >= 10) {
			p[13] = true;
			j -= 10;
			i1++;
		} else {
			p[13] = false;
		}
		p[14] = false;
		if (j >= 8) {
			p[15] = true;
			j -= 8;
			i1++;
		} else {
			p[15] = false;
		}
		if (j >= 4) {
			p[16] = true;
			j -= 4;
			i1++;
		} else {
			p[16] = false;
		}
		if (j >= 2) {
			p[17] = true;
			j -= 2;
			i1++;
		} else {
			p[17] = false;
		}
		if (j >= 1) {
			p[18] = true;
			i1++;
		} else {
			p[18] = false;
		}
		p[19] = true;
		p[20] = false;
		p[21] = false;

		if (k >= 200) {
			p[22] = true;
			k -= 200;
		} else {
			p[22] = false;
		}
		if (k >= 100) {
			p[23] = true;
			k -= 100;
		} else {
			p[23] = false;
		}
		p[24] = false;
		if (k >= 80) {
			p[25] = true;
			k -= 80;
		} else {
			p[25] = false;
		}
		if (k >= 40) {
			p[26] = true;
			k -= 40;
		} else {
			p[26] = false;
		}
		if (k >= 20) {
			p[27] = true;
			k -= 20;
		} else {
			p[27] = false;
		}
		if (k >= 10) {
			p[28] = true;
			k -= 10;
		} else {
			p[28] = false;
		}
		p[29] = true;
		if (k >= 8) {
			p[30] = true;
			k -= 8;
		} else {
			p[30] = false;
		}
		if (k >= 4) {
			p[31] = true;
			k -= 4;
		} else {
			p[31] = false;
		}
		if (k >= 2) {
			p[32] = true;
			k -= 2;
		} else {
			p[32] = false;
		}
		if (k >= 1)
			p[33] = true;
		else
			p[33] = false;
		p[34] = false;
		p[35] = false;
		if (i1 % 2 == 1)
			p[36] = true;
		else
			p[36] = false;
		if (i2 % 2 == 1)
			p[37] = true;
		else
			p[37] = false;

		if ((i3 == 15) || (i3 == 45)) {
			for (int i4 = 38; i4 < 60; i4++)
				p[i4] = false;
			return;
		}

		p[38] = false;
		p[39] = true;
		p[40] = false;

		m -= 2000;

		if (m >= 80) {
			p[41] = true;
			m -= 80;
		} else {
			p[41] = false;
		}
		if (m >= 40) {
			p[42] = true;
			m -= 40;
		} else {
			p[42] = false;
		}
		if (m >= 20) {
			p[43] = true;
			m -= 20;
		} else {
			p[43] = false;
		}
		if (m >= 10) {
			p[44] = true;
			m -= 10;
		} else {
			p[44] = false;
		}
		if (m >= 8) {
			p[45] = true;
			m -= 8;
		} else {
			p[45] = false;
		}
		if (m >= 4) {
			p[46] = true;
			m -= 4;
		} else {
			p[46] = false;
		}
		if (m >= 2) {
			p[47] = true;
			m -= 2;
		} else {
			p[47] = false;
		}
		if (m >= 1)
			p[48] = true;
		else
			p[48] = false;
		p[49] = true;

		if (n >= 4) {
			p[50] = true;
			n -= 4;
		} else {
			p[50] = false;
		}
		if (n >= 2) {
			p[51] = true;
			n -= 2;
		} else {
			p[51] = false;
		}
		if (n >= 1)
			p[52] = true;
		else
			p[52] = false;

		p[53] = false;
		p[54] = false;
		p[55] = false;
		p[56] = false;
		p[57] = false;
		p[58] = false;

		p[59] = true;
	}

	private void displayTime(Calendar cal, boolean paramBoolean) {
		int i = cal.get(Calendar.HOUR_OF_DAY);
		int j = cal.get(Calendar.MINUTE);
		int k = cal.get(Calendar.SECOND);
		gr.clearRect(0, 0, thisSize.width, thisSize.height);
		gr.drawString("Time is " + i + ":" + j + ":" + k, 20, 20);

		if (paramBoolean)
			gr.drawString("signal starts in " + (60 - k) + "sec.", 20, 40);
		else if (k == 0)
			gr.drawString("bit and value is " + k + " : M", 20, 40);
		else if (k % 10 == 9)
			gr.drawString("bit and value is " + k + " : P", 20, 40);
		else if (p[k] == true)
			gr.drawString("bit and value is " + k + " : " + 1, 20, 40);
		else {
			gr.drawString("bit and value is " + k + " : " + 0, 20, 40);
		}

		repaint();
	}

	private void displayPause(Calendar cal) {
		int i = cal.get(Calendar.HOUR_OF_DAY);
		int j = cal.get(Calendar.MINUTE);
		int k = cal.get(Calendar.SECOND);
		gr.clearRect(0, 0, thisSize.width, thisSize.height);
		gr.drawString("Time is " + i + ":" + j + ":" + k, 20, 20);
		gr.drawString("Press button to start.", 20, 40);
		repaint();
	}

	private void sleepMS(int ms) {
		try {
			if (ms < 0)
				return;
			Thread.sleep(ms);
		} catch (Exception e) {
			// empty
		}
	}

	public void run() {
		Calendar cal = Calendar.getInstance();
		int j = cal.get(Calendar.SECOND);
		boolean bool = true;

		while (isRunning) {
			cal = Calendar.getInstance();

			int i = cal.get(Calendar.SECOND);
			if (i != j) {
				j = i;

				if (isPause) {
					displayPause(cal);
					bool = true;
				} else {
					if (bool) {
						makeTimecode(cal);
						bool = false;
					} else if (i == 0) {
						makeTimecode(cal);
					}

					displayTime(cal, bool);

					if ((i == 0) || (i % 10 == 9)) {
						sourcedataline.write(wave02, 0, wave02.length);
					} else if (p[i] == true) {
						sourcedataline.write(wave05, 0, wave05.length);
					} else {
						sourcedataline.write(wave08, 0, wave08.length);
					}

					cal = Calendar.getInstance();
					sleepMS(999 - cal.get(Calendar.MILLISECOND));
				}
			}
		}
	}
}

/*
 * Location: /home/bnjf/ Qualified Name: JavaJJY JD-Core Version: 0.6.2
 */
