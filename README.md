# Snake Game

A classic terminal-based Snake game written in Java using the Lanterna library.

## Features
- Real-time arrow key controls
- Colored graphics (green snake, red food)
- Score tracking
- Smooth terminal-based gameplay

## How to Play
- Use **Arrow Keys** to move the snake
- Eat the red `*` (food) to grow and gain points
- Avoid hitting walls or yourself
- Press **Q** or **ESC** to quit

## Setup Instructions

### For Someone Cloning This Project

1. **Clone the repository:**
   ```bash
   git clone <your-repo-url>
   cd Game
   ```

2. **Open in IntelliJ IDEA:**
   - Open IntelliJ IDEA
   - File → Open → Select the `Game` folder
   - IntelliJ will automatically detect the project configuration

3. **Verify Library is Loaded:**
   - The Lanterna library (`lib/lanterna-3.1.1.jar`) is included in the repo
   - IntelliJ should automatically recognize it from `Game.iml`
   - If not, go to: File → Project Structure → Libraries → Add the jar from `lib/` folder

4. **Run the game:**
   - Open `src/Main.java`
   - Click the green Run button or press Shift+F10
   - The game will open in a terminal window

## Requirements
- Java 21 or higher
- IntelliJ IDEA (recommended) or any Java IDE
- Terminal that supports ANSI colors

## Dependencies
- Lanterna 3.1.1 (included in `lib/` folder)

## Game Controls
- ↑ ↓ ← → : Move snake
- Q or ESC : Quit game

Enjoy!
