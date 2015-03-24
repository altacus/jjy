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

		int mm = cal.get(Calendar.MINUTE);
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		int yy = cal.get(Calendar.YEAR);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;

		int hoursParity = 0;
		int minutesParity = 0;
		int mm0 = mm;

		// 0
		p[0] = true;

		// 1
		if (mm >= 40) {
			p[1] = true;
			mm -= 40;
			minutesParity++;
		} else {
			p[1] = false;
		}

		// 2
		if (mm >= 20) {
			p[2] = true;
			mm -= 20;
			minutesParity++;
		} else {
			p[2] = false;
		}

		// 3
		if (mm >= 10) {
			p[3] = true;
			mm -= 10;
			minutesParity++;
		} else {
			p[3] = false;
		}

		// 4
		p[4] = false;

		// 5
		if (mm >= 8) {
			p[5] = true;
			mm -= 8;
			minutesParity++;
		} else {
			p[5] = false;
		}

		// 6
		if (mm >= 4) {
			p[6] = true;
			mm -= 4;
			minutesParity++;
		} else {
			p[6] = false;
		}

		// 7
		if (mm >= 2) {
			p[7] = true;
			mm -= 2;
			minutesParity++;
		} else {
			p[7] = false;
		}

		// 8
		if (mm >= 1) {
			p[8] = true;
			minutesParity++;
		} else {
			p[8] = false;
		}

		// 9
		p[9] = true;

		// 10
		p[10] = false;

		// 11
		p[11] = false;

		// 12
		if (hh >= 20) {
			p[12] = true;
			hh -= 20;
			hoursParity++;
		} else {
			p[12] = false;
		}

		// 13
		if (hh >= 10) {
			p[13] = true;
			hh -= 10;
			hoursParity++;
		} else {
			p[13] = false;
		}

		// 14
		p[14] = false;

		// 15
		if (hh >= 8) {
			p[15] = true;
			hh -= 8;
			hoursParity++;
		} else {
			p[15] = false;
		}

		// 16
		if (hh >= 4) {
			p[16] = true;
			hh -= 4;
			hoursParity++;
		} else {
			p[16] = false;
		}

		// 17
		if (hh >= 2) {
			p[17] = true;
			hh -= 2;
			hoursParity++;
		} else {
			p[17] = false;
		}

		// 18
		if (hh >= 1) {
			p[18] = true;
			hoursParity++;
		} else {
			p[18] = false;
		}

		// 19
		p[19] = true;

		// 20
		p[20] = false;

		// 21
		p[21] = false;

		// 22
		if (dayOfYear >= 200) {
			p[22] = true;
			dayOfYear -= 200;
		} else {
			p[22] = false;
		}

		// 23
		if (dayOfYear >= 100) {
			p[23] = true;
			dayOfYear -= 100;
		} else {
			p[23] = false;
		}

		// 24
		p[24] = false;

		// 25
		if (dayOfYear >= 80) {
			p[25] = true;
			dayOfYear -= 80;
		} else {
			p[25] = false;
		}

		// 26
		if (dayOfYear >= 40) {
			p[26] = true;
			dayOfYear -= 40;
		} else {
			p[26] = false;
		}

		// 27
		if (dayOfYear >= 20) {
			p[27] = true;
			dayOfYear -= 20;
		} else {
			p[27] = false;
		}

		// 28
		if (dayOfYear >= 10) {
			p[28] = true;
			dayOfYear -= 10;
		} else {
			p[28] = false;
		}

		// 29
		p[29] = true;

		// 30
		if (dayOfYear >= 8) {
			p[30] = true;
			dayOfYear -= 8;
		} else {
			p[30] = false;
		}

		// 31
		if (dayOfYear >= 4) {
			p[31] = true;
			dayOfYear -= 4;
		} else {
			p[31] = false;
		}

		// 32
		if (dayOfYear >= 2) {
			p[32] = true;
			dayOfYear -= 2;
		} else {
			p[32] = false;
		}

		// 33
		if (dayOfYear >= 1) {
			p[33] = true;
		} else {
			p[33] = false;
		}

		// 34
		p[34] = false;

		// 35
		p[35] = false;

		// 36
		if (hoursParity % 2 == 1) {
			p[36] = true;
		} else {
			p[36] = false;
		}

		// 37
		if (minutesParity % 2 == 1) {
			p[37] = true;
		} else {
			p[37] = false;
		}

		// 38 - 59
		if ((mm0 == 15) || (mm0 == 45)) {
			/*
			 * Twice per hour (minutes 15 and 45), the last 20
			 * seconds of the time code are different. In lieu of
			 * the year bits, the station's call sign is broadcast
			 * at 100% modulation during seconds 40 through 48.
			 * Further, bits 50 through 55 are replaced by 6 status
			 * bits ST1 through ST6 which, if non-zero, indicate a
			 * scheduled service interruption:
			 */
			for (int i = 38; i < 60; i++) {
				p[i] = false;
			}
			return;
		}

		// 38
		p[38] = false;

		// 39
		p[39] = true;

		// 40
		p[40] = false;

		// 41
		yy -= 2000;
		if (yy >= 80) {
			p[41] = true;
			yy -= 80;
		} else {
			p[41] = false;
		}

		// 42
		if (yy >= 40) {
			p[42] = true;
			yy -= 40;
		} else {
			p[42] = false;
		}

		// 43
		if (yy >= 20) {
			p[43] = true;
			yy -= 20;
		} else {
			p[43] = false;
		}

		// 44
		if (yy >= 10) {
			p[44] = true;
			yy -= 10;
		} else {
			p[44] = false;
		}

		// 45
		if (yy >= 8) {
			p[45] = true;
			yy -= 8;
		} else {
			p[45] = false;
		}

		// 46
		if (yy >= 4) {
			p[46] = true;
			yy -= 4;
		} else {
			p[46] = false;
		}

		// 47
		if (yy >= 2) {
			p[47] = true;
			yy -= 2;
		} else {
			p[47] = false;
		}

		// 48
		if (yy >= 1) {
			p[48] = true;
		} else {
			p[48] = false;
		}

		// 49
		p[49] = true;

		// 50
		if (dayOfWeek >= 4) {
			p[50] = true;
			dayOfWeek -= 4;
		} else {
			p[50] = false;
		}

		// 51
		if (dayOfWeek >= 2) {
			p[51] = true;
			dayOfWeek -= 2;
		} else {
			p[51] = false;
		}

		// 52
		if (dayOfWeek >= 1) {
			p[52] = true;
		} else {
			p[52] = false;
		}

		// 53
		p[53] = false;

		// 54
		p[54] = false;

		// 55
		p[55] = false;

		// 56
		p[56] = false;

		// 57
		p[57] = false;

		// 58
		p[58] = false;

		// 59
		p[59] = true;
	}

	private void displayTime(Calendar cal, boolean paramBoolean) {
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		int ss = cal.get(Calendar.SECOND);

		gr.clearRect(0, 0, thisSize.width, thisSize.height);
		gr.drawString(String.format("Time is %02d:%02d:%02d", hh, mm, ss), 20, 20);

		if (paramBoolean)
			gr.drawString("signal starts in " + (60 - ss) + "sec.", 20, 40);
		else if (ss == 0)
			gr.drawString("bit and value is " + ss + " : M", 20, 40);
		else if (ss % 10 == 9)
			gr.drawString("bit and value is " + ss + " : P", 20, 40);
		else if (p[ss] == true)
			gr.drawString("bit and value is " + ss + " : " + 1, 20, 40);
		else {
			gr.drawString("bit and value is " + ss + " : " + 0, 20, 40);
		}

		repaint();
	}

	private void displayPause(Calendar cal) {
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		int ss = cal.get(Calendar.SECOND);
		gr.clearRect(0, 0, thisSize.width, thisSize.height);
		gr.drawString(String.format("Time is %02d:%02d:%02d", hh, mm, ss), 20, 20);
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
