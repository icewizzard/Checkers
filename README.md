# Getting Started with Checkers Game in Java

Welcome to the Checkers Game Java project! This guide will help you get started with writing, running, and modifying Java code in Visual Studio Code (VS Code).

## Folder Structure

The workspace contains the following folders:

- `src`: This folder contains the Java source code files. You'll be writing and modifying your game logic here.
- `lib`: This folder is meant to contain external libraries or dependencies your project might need. By default, it is empty.
- `bin`: This folder contains the compiled output files. Once you run your code, this folder will be populated with the compiled classes.

If you want to customize the folder structure, you can edit the `.vscode/settings.json` file and adjust the related settings there.

## Dependency Management

The `JAVA PROJECTS` view in VS Code allows you to manage your project's dependencies. You can easily add, remove, or update libraries through this interface. More details on dependency management in VS Code can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Running the Checkers Game

Once you have written or downloaded the code, follow these steps to run the checkers game in VS Code:

1. Open the `CheckersBoard.java` file inside the `src` folder. This is the main entry point for your checkers game.
2. Make sure the Java extension pack is installed in VS Code.
3. Run the `CheckersBoard` class by right-clicking inside the code editor and selecting `Run Java`. You can also use the play button that appears on the top right corner of the editor.

The game will open in a new window where you can play a game of checkers.

## Sample Code Snippet: Initialize Pieces for the Game

The `initializePieces()` method is responsible for initializing the positions of the checkers pieces on the board. Here's an example of how the white and black pieces are set up at the start of the game:

```java
private void initializePieces() {
    int offset = GRID_SIZE / 2;

    // Initialize white pieces on the board
    whitePieces = new Piece[]{
        new WhitePiece(1 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
        new WhitePiece(3 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
        new WhitePiece(5 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
        new WhitePiece(7 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
        new WhitePiece(0 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
        new WhitePiece(2 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
        new WhitePiece(4 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
        new WhitePiece(6 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
        new WhitePiece(1 * GRID_SIZE + offset, 2 * GRID_SIZE + offset),
        new WhitePiece(3 * GRID_SIZE + offset, 2 * GRID_SIZE + offset),
        new WhitePiece(5 * GRID_SIZE + offset, 2 * GRID_SIZE + offset),
        new WhitePiece(7 * GRID_SIZE + offset, 2 * GRID_SIZE + offset)
    };

    // Initialize black pieces on the board
    blackPieces = new Piece[]{
        new BlackPiece(0 * GRID_SIZE + offset, 5 * GRID_SIZE + offset),
        new BlackPiece(2 * GRID_SIZE + offset, 5 * GRID_SIZE + offset),
        new BlackPiece(4 * GRID_SIZE + offset, 5 * GRID_SIZE + offset),
        new BlackPiece(6 * GRID_SIZE + offset, 5 * GRID_SIZE + offset),
        new BlackPiece(1 * GRID_SIZE + offset, 6 * GRID_SIZE + offset),
        new BlackPiece(3 * GRID_SIZE + offset, 6 * GRID_SIZE + offset),
        new BlackPiece(5 * GRID_SIZE + offset, 6 * GRID_SIZE + offset),
        new BlackPiece(7 * GRID_SIZE + offset, 6 * GRID_SIZE + offset),
        new BlackPiece(0 * GRID_SIZE + offset, 7 * GRID_SIZE + offset),
        new BlackPiece(2 * GRID_SIZE + offset, 7 * GRID_SIZE + offset),
        new BlackPiece(4 * GRID_SIZE + offset, 7 * GRID_SIZE + offset),
        new BlackPiece(6 * GRID_SIZE + offset, 7 * GRID_SIZE + offset)
    };
}
