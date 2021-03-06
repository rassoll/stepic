package org.stepic.searchTrees;

import java.util.Scanner;

public class CheckThePropertyOfTheSearchTree
{
	private static Node[] nodes;

	public static void main(String[] args)
	{
		try (Scanner scanner = new Scanner(System.in))
		{
			int vertexCount = scanner.nextInt();
			if (vertexCount != 0)
			{
				nodes = new Node[vertexCount];
				for (int i = 0; i < vertexCount; i++)
				{
					nodes[i] = new Node(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
				}

				System.out.println(isTreeValid(0, Integer.MIN_VALUE, Integer.MAX_VALUE) ? "CORRECT" : "INCORRECT");
			}

			else
			{
				System.out.println("CORRECT");
			}
		}
	}

	private static boolean isTreeValid(int index, int leftBorder, int rightBorder)
	{
		if (index == -1)
		{
			return true;
		}

		if (nodes[index].value < leftBorder || nodes[index].value > rightBorder)
		{
			return false;
		}

		return isTreeValid(nodes[index].left, leftBorder, nodes[index].value)
			&& isTreeValid(nodes[index].right, nodes[index].value, rightBorder);
	}

	private static class Node
	{
		int value;
		int left;
		int right;

		private Node(int value, int left, int right)
		{
			this.value = value;
			this.left = left;
			this.right = right;
		}
	}
}
