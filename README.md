```
   __  __                                    _ _     
 |  \\/  |                 /\\               | (_)    
 | \\  / | __ _ _______   /  \\   _ __   ___ | |_ ___ 
 | |\\/| |/ _` |_  / _ \\ / /\\ \\ | '_ \\ / _ \\| | / __|
 | |  | | (_| |/ /  __// ____ \\| |_) | (_) | | \\__ \\
 |_|  |_|\\__,_/___\\___/_/    \\_\\ .__/ \\___/|_|_|___/
                               | |                  
                               |_|                  
```

# Mazeapolis
A decision making game of chances

# Building and Running
Source files are located in the `./src` directory. To build the program, run
```bash
make build
```
In order to run the program afterwards, run
```bash
make run
```

The compiled java class files are locaed in the `./dist` directory

# How to Play
(Note: To prompt an exit at any point in the program, input the letter 'q' in both the program and exit prompt. Additionally, this help dialog can be shown by pressing the 'h' key)

A game board consists of the following entities: Bonuses, Endpoints, Players, and Walls

## Basics
A player can either be Player1 or Player2. Individually, they are denoted by the characters 'P1' or 'P2'. Player1 starts at the Player1 endpoint and Player2 starts at the Player2 endpoint. The objective is to reach the opponents endpoint and have more points than your opponent. Each player starts off with one point and can gain or lose points by collecting bonuses. Players can only move in the 4 ordinal directions (north, south, east, west) and can only move onto blank spaces or spaces that contain a bonus or endpoint. Additionally, you can waste a move only if you have more than 0 points, but this move cost one point to perform. Upon moving to a space that has a bonus, that bonus will be applied to the player. You cannot move atop of other players or walls.


If you reach an endpoint but your opponent has more points, the game continues but all existing bonuses on the board will disappear. Additionally, every move that your opponent takes will cost them one point.

## Keys
- w - North
- a - West
- s - South
- d - East
- x - Waste

## How to win
You win if either:
- You reach your opponents endpoint and have more points than them
- You reach your opponents endpoint and your opponent runs out of points to move
- Your opponent does not have any moves

The game is a draw if both players reach their endpoints and have the same amount of points

## Bonuses
Bonuses can either add, subtract, multiply, or divide your points. Only mystery bonuses which are denoted by 4 question marks can divide or subtract from your points. Point additions are denoted with '+x' where 'x' is a random value between 1 and 9 inclusive. Point multiplications are denoted with 'xV' where 'V' is a random value between 2 and 3 inclusive.
