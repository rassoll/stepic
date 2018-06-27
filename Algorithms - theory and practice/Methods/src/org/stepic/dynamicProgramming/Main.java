package org.stepic.dynamicProgramming;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Main
{
	private static ArrayList<ArrayList<Integer>> results = new ArrayList<>();
	private static int minValue = Integer.MAX_VALUE;
	private static int maxValue = Integer.MIN_VALUE;
	private static int[] inputValues = getInputValues();
	private static boolean needRestoreMax = false;
	private static int counterRestoreMax = 0;
	private static int counterRestoreMaxCall = 0;
	private static int counterGetLargestList = 0;
	private static int counterGetLargestListCall = 0;
	private static int counterRemoveListByLength = 0;
	private static int counterRemoveListByLengthCall = 0;
	private static int counterAdd = 0;
	private static int counterAddCall = 0;

	private static int[] getInputValues()
	{
		Scanner scanner = new Scanner(System.in);

		int elementsNumber = scanner.nextInt();
		int[] input = new int[elementsNumber];

		for (int i = elementsNumber - 1; i >= 0; i--)
		{
			input[i] = scanner.nextInt();
		}

		maxValue = input[0];
		scanner.close();
		return input;
	}

	private static void restoreMax()
	{
		maxValue = Integer.MIN_VALUE;

		counterRestoreMaxCall++;
		for (List<Integer> list : results)
		{
			counterRestoreMax++;
			if (list != null)
			{
				int index = list.listIterator(list.size()).previous();
				if (inputValues[index] > maxValue)
				{
					maxValue = inputValues[index];
				}
			}
		}

		needRestoreMax = false;
	}

	@SuppressWarnings("unchecked")
	private static void addToResults(ArrayList<Integer> newList)
	{
		counterAddCall++;
		Object[] oResults = results.toArray();

		if (oResults.length == 0 || ((ArrayList<Integer>) oResults[0]).size() >= newList.size())
		{
			results.add(0, newList);
		}
		else if (newList.size() >= ((ArrayList<Integer>) oResults[oResults.length - 1]).size())
		{
			results.add(newList);
		}
		else
		{
			int left = 0;
			int right = oResults.length - 1;
			int middle = (left + right) / 2;

			while (left < right)
			{
				counterAdd++;
				middle = (left + right) / 2;

				if (newList.size() > ((ArrayList<Integer>) oResults[middle]).size())
				{
					left = middle + 1;
				}
				else
				{
					right = middle - 1;
				}
			}

			if (newList.size() < ((ArrayList<Integer>) oResults[left]).size())
			{
				results.add(left, newList);
			}
			else if ((newList.size() >= ((ArrayList<Integer>) oResults[left]).size())
				&& (newList.size() <= ((ArrayList<Integer>) oResults[middle]).size()))
			{
				results.add(middle, newList);
			}
			else if ((newList.size() >= ((ArrayList<Integer>) oResults[middle]).size())
				&& (newList.size() <= ((ArrayList<Integer>) oResults[right]).size()))
			{
				results.add(middle + 1, newList);
			}
			else
			{
				results.add(right + 1, newList);
			}
		}
	}

	private static List<Integer> getLargestList(int largestValue)
	{
		counterGetLargestListCall++;

		List<Integer> largestList = null;
		int length = Integer.MIN_VALUE;

		ListIterator<ArrayList<Integer>> listIterator = results.listIterator(results.size());
		while (listIterator.hasPrevious())
		{
			ArrayList<Integer> list = listIterator.previous();
			counterGetLargestList++;
			int largestListElement = inputValues[list.listIterator(list.size()).previous()];
			if (largestListElement <= largestValue)
			{
				if (length > list.size())
				{
					break;
				}
				largestList = list;
				length = list.size();
			}
		}

		return largestList;
	}

	@SuppressWarnings("unchecked")
	private static void removeListByLength(int length)
	{
		counterRemoveListByLengthCall++;
		Object[] oResults = results.toArray();

		int left = 0;
		int right = oResults.length - 1;
		int middle = (left + right) / 2;

		while (left < right)
		{
			counterRemoveListByLength++;
			middle = (left + right) / 2;

			if (length > ((ArrayList<Integer>) oResults[middle]).size())
			{
				left = middle + 1;
			}
			else
			{
				right = middle - 1;
			}
		}

		if (length < ((ArrayList<Integer>) oResults[left]).size())
		{
			middle = left;
		}
		else if ((length >= ((ArrayList<Integer>) oResults[middle]).size())
			&& (length <= ((ArrayList<Integer>) oResults[right]).size()))
		{
			middle++;
		}
		else if (length > ((ArrayList<Integer>) oResults[left]).size())
		{
			middle = right + 1;
		}

		ArrayList<Integer> list;
		while (middle < results.size() && results.get(middle).size() != length + 1)
		{
			counterRemoveListByLength++;
			list = results.get(middle);
			if (list.get(list.size() - 1) == maxValue)
			{
				needRestoreMax = true;
			}
			results.remove(middle);
		}
	}

	private static void printResults()
	{
		List<Integer> largestList = results.get(results.size() - 1);
		StringBuilder stringBuilder = new StringBuilder();

		ListIterator<Integer> listIterator = largestList.listIterator(largestList.size());

		while (listIterator.hasPrevious())
		{
			stringBuilder.append(inputValues.length - listIterator.previous());
			stringBuilder.append(" ");
		}

		System.out.println(largestList.size());
		System.out.println(stringBuilder.toString());
	}

	public static void main(String[] args)
	{
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < inputValues.length; i++)
		{
			if (inputValues[i] < minValue)
			{
				ArrayList<Integer> newList = new ArrayList<>();
				newList.add(i);
				addToResults(newList);
				minValue = inputValues[i];
			}
			else if (inputValues[i] > maxValue)
			{
				ArrayList<Integer> newList = new ArrayList<>();
				newList.addAll(results.get(results.size() - 1));
				newList.add(i);
				addToResults(newList);
				maxValue = inputValues[i];
			}
			else
			{
				ArrayList<Integer> newList = new ArrayList<>(getLargestList(inputValues[i]));
				newList.add(i);
				removeListByLength(newList.size());
				addToResults(newList);
				if (needRestoreMax)
				{
					restoreMax();
				}
			}
		}

		printResults();
		System.out.println(String.format("Time spend: %s ms", System.currentTimeMillis() - startTime));
		System.out.println(String.format("counterAdd: %s , calls: %s", counterAdd, counterAddCall));
		System.out.println(String.format("counterRestoreMax: %s , calls: %s", counterRestoreMax, counterRestoreMaxCall));
		System.out.println(String.format("counterGetLargestList: %s , calls: %s", counterGetLargestList, counterGetLargestListCall));
		System.out.println(String.format("counterRemoveListByLength: %s , calls: %s", counterRemoveListByLength, counterRemoveListByLengthCall));
	}
}
