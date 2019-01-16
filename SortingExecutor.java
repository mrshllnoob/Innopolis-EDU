package inno.l2.homework;

import java.util.Random;

/**
 * <p>Класс (@code SortingExecutor) позволяет сортировать массивы случайных целых чисел.
 *
 * Включает в себя ряд методов для работы с массивами и nested-классов, представляющих
 * собой различные виды сортировок.
 *
 * Реализовано 6 алгоритов сортировки:
 * Сортировка выбором - класс SelectionSorting
 * Сортировка вставками - класс InsertionSorting
 * Усовершенствованный алгоритм сортировки вставками - класс ImprovedInsertionSorting
 * Сортировка Шелла - класс ShellSorting
 * Нисходящая сортировка слиянием - класс DescendingMergeSorting
 * Восходящая сортировка слиянием - класс AscendingMergeSorting
 *
 * @author Tsapin Anton
 */
public class SortingExecutor {

    private static Comparable[] values;
    private static SortingAlgorithm alg;
    private static final int arrLen = 60000;
    public static final int randomRange = 60000;

    /**
     * Метод main является точкой входа в программу.
     *
     * Для осуществления сортировки в нём необходимо создать экземпляр класса
     * конкретного алгоритма:
     * <blockquote><pre>
     *      alg = new ИмяКлассаКонкретнойСортировки;
     * </pre></blockquote><p>
     *
     *
     */
    public static void main(String[] args) {
        alg = new AscendingMergeSorting();
        values = generateIntegersArray(arrLen);
        show(values);
        alg.sort();
        assert isSorted(values);
        show(values);
    }

    /**
     * Генерирует массив значений класса Integer указанной длины
     *
     * @param arrLen длина генерируемого массива
     * @return возвращает ссылку на сгенерированный целочисленный массив
     */
    private static Comparable[] generateIntegersArray(int arrLen) {
        Random rand = new Random();
        Integer[] temp = new Integer[arrLen];
        for(int i=0;i<arrLen;i++)
            temp[i] = rand.nextInt(randomRange);
        return temp;
    }

    /**
     * Сравнивает Comparable-объекты
     *
     * @param comp1 ссылка на объект 1
     * @param comp2 ссылка на объект 2
     * @return возвращает true, если объект 1 меньше объекта 2; false, если объект 1 больше или равен объекту 2
     */
    public static boolean less(Comparable comp1, Comparable comp2) {
        return comp1.compareTo(comp2)<0;
    }

    /**
     * Метод меняет местами элементы указанного массива по переданным индексам
     *
     * @param comp ссылка на объект массива
     * @param i индекс элемента 1
     * @param j индекс элемента 2
     */
    public static void exchangeElements(Comparable[] comp, int i,int j) {
        Comparable temp = comp[i];
        comp[i] = comp[j];
        comp[j] = temp;
    }

    /**
     * Вывод массива в stdout
     *
     * @param comp ссылка на объект выводимого в stdout массива
     */
    private static void show(Comparable[] comp) {
        for(int i=0;i<comp.length;i++)
            System.out.print(comp[i] + " ");
        System.out.println();
    }

    /**
     * Проверяет отсортирован ли массив по переданной ссылке.
     *
     * @param comp ссылка на проверяемый массив.
     * @return возвращает true, если отсортировано и false, если нет
     */
    public static boolean isSorted(Comparable[] comp) {
        for(int i=1;i<comp.length;i++)
            if (less(comp[i], comp[i-1]))
                return false;
        return true;
    }

    /**
     * Класс в методе (@code sort()) которого реализован алгоритм сортировки вставками.
     * Реализует интерфейс SortingAlgorithm.
     */
    private static class InsertionSorting implements SortingAlgorithm {

        /**
         * Сортировка вставками. Модифицирует статическое поле класса SortingExecutor.
         */
        @Override
        public void sort() {
            for(int i=1;i<arrLen;i++)
                for(int j=i;j>0 && less(values[j],values[j-1]);j--)
                    exchangeElements(values, j,j-1);
        }
    }

    /**
     * Класс в методе (@code sort()) которого реализован усовершенствованный алгоритм сортировки вставками.
     * Реализует интерфейс SortingAlgorithm.
     */
    private static class ImprovedInsertionSorting implements SortingAlgorithm {
        /**
         * Усовершенствованная сортировка вставками.
         */
        @Override
        public void sort() {
            int exchanges = 0;
            for (int i = arrLen-1; i > 0; i--) {
                if (less(values[i], values[i-1])) {
                    exchangeElements(values, i, i-1);
                    exchanges++;
                }
            }
            if (exchanges == 0) return;

            for (int i = 2; i < arrLen; i++) {
                Comparable v = values[i];
                int j = i;
                while (less(v, values[j-1])) {
                    values[j] = values[j-1];
                    j--;
                }
                values[j] = v;
            }
        }
    }

    /**
     * Класс в методе (@code sort()) которого реализован алгоритм сортировки выбором.
     * Реализует интерфейс SortingAlgorithm.
     */
    private static class SelectionSorting implements SortingAlgorithm {

        /**
         * Сортировка выбором.
         */
        @Override
        public void sort() {
            for(int i=0;i<arrLen;i++) {
                int min = i;
                for(int j=i+1;j<arrLen;j++)
                    if(less(values[j], values[min]))
                        min = j;
                exchangeElements(values,i,min);
            }
        }
    }

    /**
     * Класс в методе (@code sort()) которого реализован алгоритм сортировки Шелла.
     * Реализует интерфейс SortingAlgorithm.
     */
    private static class ShellSorting implements SortingAlgorithm {

        /**
         * Сортировка Шелла
         */
        @Override
        public void sort() {
            int h = 1;
            while(h<arrLen/3)
                h = 3*h + 1;

            while(h>=1) {
                for(int i=h; i<arrLen; i++) {
                    for(int j=i;j>=h && less(values[j],values[j-h]);j-=h)
                        exchangeElements(values,j,j-h);
                }
                h = h/3;
            }
        }
    }

    /**
     * Класс, содержащий реализацию алгоритма нисходящей сортировки слиянием.
     *
     * Имеет перегруженный метод (@code sort())
     * Наследует класс MergeSorting и реализует интерфейс SortingAlgorithm
     */
    private static class DescendingMergeSorting extends MergeSorting {

        /**
         * Реализация метода (@code sort()) интерфейса SortingAlgorithm.
         *
         * Здесь задается размер временного массива (@code temp) и вызывается
         * метод (@code sort(int,int)) с аргументами, указывающими на начало и
         * конец сортируемого массива.
         */
        @Override
        public void sort() {
            temp = new Comparable[values.length];
            sort( 0, arrLen - 1);
        }

        /**
         * Реализация алгоритма нисходящей сортировки слиянием.
         *
         * Метод модифицирует статическое поле (@code values)
         * класса (@code SortingExecutor).
         * @param lowestIndex наименьшей индекс массива
         * @param highestIndex наибольший индекс массива
         */
        private void sort(int lowestIndex, int highestIndex) {
            if (highestIndex<=lowestIndex) return;
            int middleIndex = lowestIndex + (highestIndex - lowestIndex)/2;
            sort(lowestIndex, middleIndex);
            sort(middleIndex + 1, highestIndex);
            merge(lowestIndex, middleIndex, highestIndex);
        }
    }

    /**
     * Класс, содержащий реализацию алгоритма восходящей сортировки слиянием.
     *
     * Имеет перегруженный метод (@code sort())
     * Наследует класс MergeSorting и реализует интерфейс SortingAlgorithm
     */
    private static class AscendingMergeSorting extends MergeSorting {
        /**
         * Реализация алгоритма нисходящей сортировки слиянием.
         *
         * Метод модифицирует статическое поле (@code values)
         * класса (@code SortingExecutor).
         */
        @Override
        public void sort() {
            temp = new Comparable[arrLen];
            for(int subArraySize = 1; subArraySize<arrLen; subArraySize += subArraySize)
                for(int lowestIndex = 0; lowestIndex < arrLen - subArraySize; lowestIndex += subArraySize*2)
                    merge(lowestIndex,
                            lowestIndex+subArraySize-1, Math.min(lowestIndex + subArraySize*2-1,arrLen-1));
        }
    }

    /**
     * Абстрактный класс для алгоритмов сортировки слиянием.
     *
     * Имеет метод (@code merge()) отвечающий за слияние временного (@code temp)
     * и сортируемого массивов.
     * Реализует интерфейс SortingAlgorithm.
     */
    private abstract static class MergeSorting implements SortingAlgorithm{
        public Comparable[] temp;

        public final void merge(int lowestIndex, int middleIndex, int highestIndex) {
            int i = lowestIndex;
            int j = middleIndex + 1;
            for(int k=lowestIndex;k<=highestIndex;k++)
                temp[k] = values[k];
            for(int k = lowestIndex;k<=highestIndex; k++)
                if (i>middleIndex)
                    values[k] = temp[j++];
                else if (j>highestIndex)
                    values[k] = temp[i++];
                else if (less(temp[j],temp[i]))
                    values[k] = temp[j++];
                else
                    values[k] = temp[i++];
        }
    }
}
