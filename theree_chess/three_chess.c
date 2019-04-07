#include "three_chess.h"

void ShowUI()
{
	printf("****************************\n");
	printf("*****     1.play!     ******\n");
	printf("*****     0.exit!     ******\n");
	printf("****************************\n");
}

void ComputerMove(char board[ROW][COL], int row, int col)
{
	while (1)
	{
		int x = rand() % row;
		int y = rand() % col;
		if (board[x][y] == ' ')
		{
			board[x][y] = 'O';
			break;
		}
	}
}

void PlayerMove(char board[ROW][COL], int row, int col)
{
	int x, y;
	while (1)
	{
		printf("请输入你所下的棋的位置pos(x,y):>");
		scanf("%d %d", &x, &y);
		if (x >= 1 && x <= row&&y >= 1 && y <= col)
		{
			if (board[x-1][y-1] == ' ')
			{
				board[x-1][y-1] = 'X';
				break;
			}
			else printf("位置不对，请重新输入!\n");
		}
		else printf("位置不对，请重新输入!\n");
	}
}

void ShowBoard(char board[ROW][COL], int row, int col)
{
	int i, j;
	printf("   1  2  3\n");
	printf(" -----------\n");
	for (i = 1; i <= row; i++)
	{
		printf("%d |", i);
		for (j = 1; j <= col; j++)
		{
			printf("%c |", board[i - 1][j - 1]);
		}
		printf("\n");
	}
}

void Game()
{
	char board[ROW][COL];
	char result = 'N';
	memset(board, ' ', sizeof(board));
	srand((unsigned long )time(NULL));
	while (1)
	{
		 ComputerMove(board,ROW,COL);
		 ShowBoard(board, ROW, COL);
		 result = Judge(board, ROW, COL);
		 if (result != 'N')
			 break;
		 PlayerMove(board, ROW, COL);
		 ShowBoard(board, ROW, COL);
		 result = Judge(board, ROW, COL);
		 if (result != 'N')
			 break;
	}
	switch (result)
	{
	case 'X':
		printf("你赢了！\n");
		break;
	case 'O':
		printf("电脑赢了!\n");
		break;
	case 'E':
		printf("平局！\n");
		break;
	default:
		break;
	}
}

char Judge(char board[ROW][COL], int row, int col)
{
	int i = 0;
	int j = 0;
	for (i=0; i < row; i++)
	{
		if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && \
			board[i][0] != ' ')
		
			return board[i][0];
		
	}
	for (i = 0; i < col; i++)
	{
		if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && \
			board[0][i] != ' ')
		
			return board[0][i];
		
	}
	if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && \
		board[1][1] != ' ')
	
		return board[0][0];
	
	if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && \
		board[1][1] != ' ')
	
		return board[0][2];
	

	for (i = 0; i < row; i++)
	{
		for (j = 0; j < col; j++)
		{
			if (board[i][j] == ' ')
			{
				return 'N';
			}
		}
	}
	return 'E';
}
