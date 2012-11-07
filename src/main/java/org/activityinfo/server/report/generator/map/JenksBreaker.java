package org.activityinfo.server.report.generator.map;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;

/**
 * The Jenks optimization method, also called the Jenks natural breaks classification method,
 * is a data classification method designed to determine the best arrangement of values into
 * different classes. This is done by seeking to minimize each class’s average deviation from 
 * the class mean, while maximizing each class’s deviation from the means of the other groups. 
 * In other words, the method seeks to reduce the variance within classes and maximize the 
 * variance between classes.
 *
 */
public class JenksBreaker {

	private LinkedList<Double> list = Lists.newLinkedList();
	private int[] classes;

	public void addValue(double value) {
		list.add(value);
	}

	public void addValues(double[] values) {
		for(double value : values) {
			addValue(value);
		}
	}


	/**
	 * @return int[] breaks (upper indices of class)
	 * @param list sorted list of values
	 * @param numclass int number of classes
	 */
	public Breaks getJenksBreaks(int numclass) {

		List<Double> list = Lists.newArrayList(this.list);
		Collections.sort(list);

		int numdata = list.size();


		double[][] mat1 = new double[numdata + 1][numclass
		                                          + 1];
		double[][] mat2 = new double[numdata + 1][numclass
		                                          + 1];

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

				double val = ((Double)list.get(i3-1)).doubleValue();

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

		kclass[numclass - 1] = list.size() - 1;

		for (int j = numclass; j >= 2; j--) {
			int id =  (int) (mat1[k][j]) - 2;

			kclass[j - 2] = id;

			k = (int) mat1[k][j] - 1;
		};
		return new Breaks(list, kclass);
	}

	public double [] getBreaks(int[] pos) {
		double breaks[] = new double[pos.length];
		for(int i=0;i!=pos.length;++i) {
			breaks[i] = list.get(pos[i]);
		}
		return breaks;
	}

	private static class Breaks {

		private List<Double> dataList;
		private int[] breaks;

		public Breaks(List<Double> list, int[] kclass) {
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
			double listMean = mean(dataList);
			double SDAM = 0.0;
			for(int i=0;i!=dataList.size();++i) {
				double sqDev = Math.pow(dataList.get(i) - listMean, 2);
				SDAM += sqDev;

			}
			double SDCM = 0.0;
			for(int i=0;i!=numClass();++i) {
				List<Double> classList = classList(i);
				double classMean = mean(classList);
				double preSDCM = 0.0;
				for(int j = 0; j!=classList.size();++j) {
					double sqDev2 = Math.pow(classList.get(j) - classMean, 2);
					preSDCM += sqDev2;
					SDCM += preSDCM;
				}
			}
			return (SDAM - SDCM)/SDAM;
		}

		private List<Double> classList(int i) {
			int classStart = (i == 0) ? 0 : breaks[i-1];
			int classEnd = breaks[i];
			return dataList.subList(classStart, classEnd+1);
		}
		
		/**
		 * @param classIndex
		 * @return the minimum value (inclusive) of the given class
		 */
		public double getClassMin(int classIndex) {
			if(classIndex == 0) {
				return dataList.get(0);
			} else {
				return dataList.get(breaks[classIndex-1]);
			}
		}
		
		/**
		 * @param classIndex
		 * @return the maximum value (inclusive) of the given class
		 */
		public double getClassMax(int classIndex) {
			return dataList.get(breaks[classIndex]);
		}
		
		public int getClassCount(int classIndex) {
			if(classIndex == 0) {
				return breaks[0]+1;
			} else {
				return breaks[classIndex]-breaks[classIndex-1];
			}
		}
		
		private double mean(List<Double> values) {
			double sum = 0;
			for(int i=0;i!=values.size();++i) {
				sum += values.get(i);
			}
			return sum / (double)values.size();
		}

		private int numClass() {
			return breaks.length;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(int i=0;i!=numClass();++i) {
				if(getClassMin(i) == getClassMax(i)) {
					sb.append(getClassMin(i));
				} else {
					sb.append(getClassMin(i)).append(" - ").append(getClassMax(i));
				}
				sb.append(" (" + getClassCount(i) + ")");
				sb.append("\n");
			}
			return sb.toString();
		}
	}
}