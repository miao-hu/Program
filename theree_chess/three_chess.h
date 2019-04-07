#ifndef _THREE_CHESS_H_
#define _THREE_CHESS_H_
#define _CRT_SECURE_NO_WARNINGS 1

#include<stdio.h>
#include<stdlib.h>
#include<windows.h>
#include<time.h>

#define ROW 3
#define COL 3

void ShowUI();
void Game();
void ComputerMove(char board[ROW][COL], int row, int col);
void ShowBoard(char board[ROW][COL], int row, int col);
void PlayerMove(char Bobrd[ROW][COL], int row, int col);
char Judge(char board[ROW][COL], int row, int col);

#endif
