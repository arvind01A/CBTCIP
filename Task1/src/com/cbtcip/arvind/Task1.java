package com.cbtcip.arvind;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Task1 {

	public static void main(String[] args) {
		Task1 task1 = new Task1();
		task1.guessTheNumber();
		
	}//main
	
	public void guessTheNumber() {
		try(Scanner sc = new Scanner(System.in)){
			
			// generate a random number between 1 to 100
			int answer = (int) (Math.random() * 100) + 1; 
			
			// number of trials that user has to guess the number
			int k = 3;

			System.out.println("I'm thinking of a number between 1 to 100.\nYou have " + k + " tries to guess the number.");
			
			// Read user input and random generated number
			while(k>0) {
				System.out.println("\nEnter your guess: ");
				int guess = sc.nextInt();
				
				
				// if the user guesses correctly, print the congratulation message and exit the program
				if(guess < 0 || guess > 100) {
					System.out.println("Invalid number/nchoose between 1 to 100");
					k--;
				} else if(guess == answer) {
					System.out.println("Congratulation! \n You win!");
					break;
				} else if (guess > answer) {
					System.out.println("You guess is too high. ");
					if(k>1)
						System.out.println( (k - 1) + " Attempt left. ");
					k--;
				} else if(guess < answer) {
					System.out.println("You guess is too low.");
					if(k>1)
						System.out.println( (k - 1) + " Attempt left.");
					k--;
				} 
				
				if(k == 0) {
					System.out.println();
					System.err.println("                          Game Over!                          ");
					System.err.println("                  Next time Better luck                     ");
					break;
				}
				
			}
		} catch(InputMismatchException ime) {
			System.err.println("Guess only integer number");
			ime.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} //try-catch
		
	}//guessTheNumber()

}//class
