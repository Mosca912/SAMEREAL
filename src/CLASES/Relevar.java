/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASES;

/**
 *
 * @author Facuymayriver
 */
public class Relevar {
    /*
    SELECT 
    idtripulacion,
    SEC_TO_TIME(
        TIME_TO_SEC(
            CASE 
                WHEN MAX(llegada) < MIN(salida) 
                    THEN ADDTIME(MAX(llegada), '24:00:00') 
                ELSE MAX(llegada) 
            END
        ) - TIME_TO_SEC(MIN(salida))
    ) AS tiempo_total
FROM movimientos
WHERE idtripulacion = 4
GROUP BY idtripulacion;
    
    SELECT *
FROM movimientos
WHERE idtripulacion IN (
    6,
    (SELECT MAX(idtripulacion) FROM movimientos WHERE idtripulacion < 6)
);
    
    SELECT 
    idtripulacion,
    MAX(kilometrosdesalida), 
    MIN(kilometrosdesalida)
FROM movimientos
WHERE idtripulacion = 4
GROUP BY idtripulacion;
*/
}
