import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame {
	
	private int remainingGuesses;
	private String wrongGuesses;
	private String[] words = {"cat", "dog", "hippo", "mouse", "squid"};//changed Brett
	private String[] hints = {"Garfield!",
							"Man's best friend!",
							"One of the most dangerous animals in the world!",
							"Jerry, in Tom and Jerry!",
							"Spongebob's neighbor!"}; 
	private int index = 0;//changed Brett
	private String word;
	private String visible;
	private boolean playAgain = false;

	public MainWindow() {//changed Brett
		remainingGuesses = 10;
		wrongGuesses = "";
		visible = "";
		word = words[index];

		for(int i = 0; i < word.length(); ++i) {
			visible += "_ ";
		}

		
		JPanel corePanel = new JPanel();
		corePanel.setLayout(new BorderLayout());
		
		final JLabel output = new JLabel("Guess a letter.");
		final JLabel status = new JLabel("You have "+remainingGuesses+" remaining", SwingConstants.CENTER);
		final JLabel wrong = new JLabel("Wrong guesses so far: "+wrongGuesses);
		final JLabel visibleLabel = new JLabel(visible, SwingConstants.CENTER);
		final JLabel hintText = new JLabel("Hint: " + hints[index]);//changed Brett
		final JTextField input = new JTextField(); 
		
		JPanel southPanel = new JPanel(new GridLayout(6, 1));//changed Brett
		southPanel.add(hintText);//changed Brett
		southPanel.add(status);
		southPanel.add(visibleLabel);
		southPanel.add(output);
		southPanel.add(input);
		southPanel.add(wrong); 

		
		corePanel.add(southPanel, BorderLayout.SOUTH);
		
		final HangmanFigure hf = new HangmanFigure();
		corePanel.add(hf, BorderLayout.CENTER);

		this.add(corePanel, BorderLayout.CENTER);
		
		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String text = input.getText();
				
				if(text.length()  == 1 && text.matches("[a-z]")) {
					
					boolean guessFound = false;
					
					for(int i = 0; i < word.length(); ++i) {
						if(text.charAt(0) == word.charAt(i)) {
							guessFound = true;
							if(visible.contains(text)){
								output.setText("You already guessed that.");
							}else{
							output.setText("Goog job! Guess again.");
							}
							
							String newVisible = "";
							for(int j = 0; j < visible.length(); ++j) {
								if(j == (i*2)) {
									newVisible += word.charAt(i);
								}
								else {
									newVisible += visible.charAt(j);
								}
							}
							visible = newVisible;
							visibleLabel.setText(visible);
						}
					}
					
					if(!guessFound) {
						if(wrongGuesses.contains(text)){
							output.setText("You already guessed that.");
						}
						else if(--remainingGuesses > 0) {
							output.setText("Nope! Guess again.");
							status.setText("You have "+remainingGuesses+" guesses remaining");
							wrongGuesses += text+" ";
							wrong.setText("Wrong guesses so far: "+wrongGuesses);
							hf.set();
						}
						else {
							status.setText("You have "+remainingGuesses+" remaining");
							output.setText("You lost: the word was "+word + ". Play again?(yes/no)");
							playAgain = true;
						}
					}
					else {
						String actualVisible = "";
						for(int i = 0; i < visible.length(); i+=2) {
							actualVisible += visible.charAt(i);
						}
						
						if(actualVisible.equals(word)) {
							output.setText("Congratulations, you won!" + " Play again?(yes/no)");
							playAgain = true;
						}
					}
					
				}
				else if (playAgain && (text.equals("yes") || text.equals("no"))){
					if(text.equals("yes")){
						hintText.setText("Hint: " + hints[index]);
						reset();
					}
					else{
						output.setText("Thanks for playing!");
						input.setEnabled(false);
					}
				}
					
				else {
					if(text.matches("[A-Z]")){
						output.setText("Lowercase letters only.");
					}else{
						output.setText("Invalid input!");
					}
				}
				
				input.setText("");
			}
			
		});
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainWindow();
	}
	
	public void reset(){
		index++;
		remainingGuesses = 10;
		wrongGuesses = "";
		visible = "";
		word = words[index];
	}
}
