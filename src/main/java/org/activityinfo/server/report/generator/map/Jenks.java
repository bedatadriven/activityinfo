package org.activityinfo.server.report.generator.map;

import java.util.Arrays;
import java.util.LinkedList;

import com.google.common.collect.Lists;


/**
 * The Jenks optimization method, also called the Jenks natural breaks classification method,
 * is a data classification method designed to determine the best arrangement of values into
 * different classes. This is done by seeking to minimize each class’s average deviation from 
 * the class mean, while maximizing each class’s deviation from the means of the other groups. 
 * In other words, the method seeks to reduce the variance within classes and maximize the 
 * variance between classes.
 *
 */
public class Jenks {

	private LinkedList<Double> list = Lists.newLinkedList();

	public void addValue(double value) {
		list.add(value);
	}

	public void addValues(double... values) {
		for(double value : values) {
			addValue(value);
		}
	}

	/**
	 * 
	 * @return 
	 */
	public Breaks computeBreaks() {
		double[] list = toSortedArray();
		
		int uniqueValues = countUnique(list);
		if(uniqueValues <= 3) {
			return computeBreaks(list, uniqueValues);
		}
		
		Breaks lastBreaks = computeBreaks(list, 3);
		double lastGvf = lastBreaks.gvf();
		double lastImprovement = lastGvf - computeBreaks(list, 2).gvf();
		
		for(int i=3;i<=Math.min(6, uniqueValues);++i) {
			Breaks breaks = computeBreaks(list, 3);
			double gvf = breaks.gvf();
			double marginalImprovement = gvf - lastGvf;
			if(marginalImprovement < lastImprovement) {
				return lastBreaks;
			}
			lastBreaks = breaks;
			lastGvf = gvf;
			lastImprovement = marginalImprovement;
		}
		
		return lastBreaks;
	}

	private double[] toSortedArray() {
		double values[] = new double[this.list.size()];
		for(int i=0;i!=values.length;++i) {
			values[i] = this.list.get(i);
		}
		Arrays.sort(values);
		return values;
	}

	private int countUnique(double[] sortedList) {
		int count = 1;
		for(int i=1;i<sortedList.length;++i) {
			if(sortedList[i] != sortedList[i-1]) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @return int[] breaks (upper indices of class)
	 * @param list sorted list of values
	 * @param numclass int number of classes
	 */
	public Breaks computeBreaks(int numclass) {
		return computeBreaks(toSortedArray(), numclass, new Identity());
	}
	
	private Breaks computeBreaks(double[] list, int numclass) {
		return computeBreaks(list, numclass, new Identity());
	}

	
	private Breaks computeBreaks(double[] list, int numclass, DoubleFunction transform) {
		
		int numdata = list.length;

		

		double[][] mat1 = new double[numdata + 1][numclass + 1];
		double[][] mat2 = new double[numdata + 1][numclass + 1];

		for (int i = 1; i <= numclass; i++) {
			mat1[1][i] = 1;
			mat2[1][i] = 0;
			for (int j = 2; j <= numdata; j++)
				mat2[j][i] = Double.MAX_VALUE;
		}
		double v = 0;
		for (int l = 2; l <= numdata; l++) {
			double s1 = 0;
			double s2 = 0;
			double w = 0;
			for (int m = 1; m <= l; m++) {
				int i3 = l - m + 1;

				double val = transform.apply(list[i3-1]);

				s2 += val * val;
				s1 += val;

				w++;
				v = s2 - (s1 * s1) / w;
				int i4 = i3 - 1;
				if (i4 != 0) {
					for (int j = 2; j <= numclass; j++) {
						if (mat2[l][j] >= (v + mat2[i4][j - 1])) {
							mat1[l][j] = i3;
							mat2[l][j] = v + mat2[i4][j - 1];
						};
					};
				};
			};
			mat1[l][1] = 1;
			mat2[l][1] = v;
		};
		int k = numdata;


		int[] kclass = new int[numclass];

		kclass[numclass - 1] = list.length - 1;

		for (int j = numclass; j >= 2; j--) {
			int id =  (int) (mat1[k][j]) - 2;

			kclass[j - 2] = id;

			k = (int) mat1[k][j] - 1;
		};
		return new Breaks(list, kclass);
	}
	
	private interface DoubleFunction {
		double apply(double x);
	}
	
	private static class Log10 implements DoubleFunction {

		@Override
		public double apply(double x) {
			return Math.log10(x);
		}
	}
	
	public static class Identity implements DoubleFunction {

		@Override
		public double apply(double x) {
			return x;
		}
		
	}


	public static class Breaks {

		private double[] dataList;
		private int[] breaks;

		private Breaks(double[] list, int[] kclass) {
			this.dataList = list;
			this.breaks = kclass;
		}

		/**
		 * The Goodness of Variance Fit (GVF) is found by taking the 
		 * difference between the squared deviations
		 * from the array mean (SDAM) and the squared deviations from the 
		 * class means (SDCM), and dividing by the SDAM
		 * @return
		 */
		public double gvf() { 
			double SDAM = sumOfSquareDeviations(dataList);
			double SDCM = 0.0;
			for(int i=0;i!=numClassses();++i) {
				SDCM += sumOfSquareDeviations(classList(i));
			}
			return (SDAM - SDCM)/SDAM;
		}

		private double sumOfSquareDeviations(double[] values) {
			double mean = mean(values);
			double sum = 0.0;
			for(int i=0;i!=values.length;++i) {
				double sqDev = Math.pow(values[i] - mean, 2);
				sum += sqDev;
			}
			return sum;
		}
		
		public double[] getValues() {
			return dataList;
		}

		private double[] classList(int i) {
			int classStart = (i == 0) ? 0 : breaks[i-1]+1;
			int classEnd = breaks[i];
			double list[] = new double[classEnd-classStart+1];
			for(int j=classStart;j<=classEnd;++j) {
				list[j-classStart] = dataList[j];			
			}
			return list;
		}
		
		/**
		 * @param classIndex
		 * @return the minimum value (inclusive) of the given class
		 */
		public double getClassMin(int classIndex) {
			if(classIndex == 0) {
				return dataList[0];
			} else {
				return dataList[breaks[classIndex-1]+1];
			}
		}
		
		/**
		 * @param classIndex
		 * @return the maximum value (inclusive) of the given class
		 */
		public double getClassMax(int classIndex) {
			return dataList[breaks[classIndex]];
		}
		
		public int getClassCount(int classIndex) {
			if(classIndex == 0) {
				return breaks[0]+1;
			} else {
				return breaks[classIndex]-breaks[classIndex-1];
			}
		}
		
		private double mean(double[] values) {
			double sum = 0;
			for(int i=0;i!=values.length;++i) {
				sum += values[i];
			}
			return sum / (double)values.length;
		}

		public int numClassses() {
			return breaks.length;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(int i=0;i!=numClassses();++i) {
				if(getClassMin(i) == getClassMax(i)) {
					sb.append(getClassMin(i));
				} else {
					sb.append(getClassMin(i)).append(" - ").append(getClassMax(i));
				}
				sb.append(" (" + getClassCount(i) + ")");
				sb.append(" = ").append(Arrays.toString(classList(i)));
				sb.append("\n");
			}
			return sb.toString();
		}

		public int classOf(double value) {
			for(int i=0;i!=numClassses(); ++i) {
				if(value <= getClassMax(i)) {
					return i;
				}
			}
			return numClassses()-1;
		}
	}
}