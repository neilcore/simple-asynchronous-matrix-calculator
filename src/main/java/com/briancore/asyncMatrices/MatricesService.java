package com.briancore.asyncMatrices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Service
public class MatricesService {

    @Async
    public CompletableFuture<HashMap<String, Object>> matricesInformation(
            Object matrix,
            int[] elementOf
    ) throws JsonProcessingException {
        ObjectMapper jacksonMapper = new ObjectMapper();
        HashMap<String, Object> result = new HashMap<>();

        int row = elementOf[0] - 1;
        int column = elementOf[1] - 1;

        result.put("matrix", jacksonMapper.writeValueAsString(matrix));

        /* for one row matrix */
        if (matrix instanceof int[] oneRowMatrix) {
            result.put("type", "full squared matrix");
            result.put("order", "1x1");
            result.put("row",1);
            result.put("column", ((int[]) matrix).length);
            if (elementOf[0] - 1 > 1) {
                throw new IllegalArgumentException("Invalid given element. order of the matrix is 1x" + oneRowMatrix.length);
            }
            result.put("elementOf", "M " + elementOf[0] + " to " + elementOf[1]);
            int elementOfValue = oneRowMatrix[elementOf[1] - 1]; /* get the result of the elementOf */

            result.put("value", elementOfValue);
            return CompletableFuture.completedFuture(result);

        } else if (matrix instanceof int[][] multipleRowMatrix) {
            /* for two or more row matrices */
            /* check if each column length in every row are equal*/
            int nextRow;
            for (int r = 0; r < multipleRowMatrix.length; r++) {
                nextRow = r + 1;
                if (r == multipleRowMatrix.length - 1) break;
                if (multipleRowMatrix[r].length != multipleRowMatrix[nextRow].length) {
                    throw new IllegalArgumentException("Invalid matrix.");
                }
            }
            /* check matrix type */
            /* needs to recheck in the future */
            if (multipleRowMatrix[0].length != multipleRowMatrix.length) {
                result.put("type", "non squared matrix");
            } else {
                result.put("type", "full squared matrix");
            }

            result.put("elementOf", "M " + elementOf[0] + " to " + elementOf[1]);
            int elementOfValue = multipleRowMatrix[row][column]; /* result of elementOf */
            result.put("value", elementOfValue);
            result.put("order", multipleRowMatrix.length + "x" + multipleRowMatrix[0].length);
            return CompletableFuture.completedFuture(result);

        }
        return null;
    }
    /* will display the error */
    public static void displayCustomError() {
        throw new IllegalArgumentException("Invalid Matrices");
    }
    /* this method will check the equality of row and columns --NEEDED FOR COMPUTATION-- */
    public static void checkMatricesEquality(Object matrixA, Object matrixB) {
        if (matrixA instanceof int[] ONE_ROW_MATRIX_A && matrixB instanceof int[] ONE_ROW_MATRIX_B) {
            if (ONE_ROW_MATRIX_A.length != ONE_ROW_MATRIX_B.length) {
                throw new IllegalArgumentException(
                        "Invalid matrices. "
                        + "ONE_ROW_MATRIX_A length " + ONE_ROW_MATRIX_A.length
                        + " ONE_ROW_MATRIX_B length " + ONE_ROW_MATRIX_B.length
                );
            }
        } else if (
                matrixA instanceof int[][] MULTI_ROW_MATRIX_A
                && matrixB instanceof int[][] MULTI_ROW_MATRIX_B
        ) {

            /* check row equality */
            if (MULTI_ROW_MATRIX_A.length != MULTI_ROW_MATRIX_B.length) {
                displayCustomError();
            }
            /* check column equality */
            for (int r = 0; r < MULTI_ROW_MATRIX_A.length; r++) {
                if (r == MULTI_ROW_MATRIX_A.length - 1) break;
                if(MULTI_ROW_MATRIX_A[r].length != MULTI_ROW_MATRIX_A[r + 1].length) {
                    displayCustomError();
                }
            }
            for (int r = 0; r < MULTI_ROW_MATRIX_B.length; r++) {
                if (r == MULTI_ROW_MATRIX_B.length - 1) break;
                if(MULTI_ROW_MATRIX_B[r].length != MULTI_ROW_MATRIX_B[r + 1].length) {
                    displayCustomError();
                }
            }

        }
    }
}
