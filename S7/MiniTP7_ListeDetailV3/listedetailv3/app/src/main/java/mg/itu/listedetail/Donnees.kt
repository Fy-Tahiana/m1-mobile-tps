package mg.itu.listedetail

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow


/**
 * Mini-TP 7 — « Trois requêtes »
 *
 * La couche données de l'application, avec Room.
 * Trois éléments à connaître, et c'est tout :
 *   - l'ENTITY  : une table, décrite par une data class
 *   - le DAO    : les requêtes, décrites par des fonctions annotées
 *   - la DATABASE : le point d'assemblage
 *
 * Votre travail : les TROIS TODO du DAO. Rien d'autre n'est à modifier.
 */

// ---------------------------------------------------------------------------
// L'ENTITY — une table « produits », une ligne par produit
// ---------------------------------------------------------------------------

@Entity(tableName = "produits")
data class Produit(
    @PrimaryKey val id: Int,
    val nom: String,
    val origine: String,
    val prixKg: Double?,
    val stockKg: Double,
)

@Dao
interface ProduitDao {

    /** Fourni : tous les produits, triés par nom. */
    @Query("SELECT * FROM produits ORDER BY nom ASC")
    fun tousLesProduits(): Flow<List<Produit>>

    /** Fourni : un produit par son identifiant. */
    @Query("SELECT * FROM produits WHERE id = :id")
    suspend fun parId(id: Int): Produit?

    /** Fourni : insertion du jeu de données initial. */
    @Insert
    suspend fun insererTous(produits: List<Produit>)

    // TODO 1 fait — TRI du plus cher au moins cher, NULL en dernier :
    @Query("SELECT * FROM produits ORDER BY prixKg IS NULL, prixKg DESC")
    fun parPrixDecroissant(): Flow<List<Produit>>

    // TODO 2 fait — FILTRE paramétré : stock au-dessus d'un seuil :
    @Query("SELECT * FROM produits WHERE stockKg > :seuilKg")
    fun stockSuperieurA(seuilKg: Double): Flow<List<Produit>>

    // TODO 3 fait — AGRÉGAT : stock total, null si la table est vide :
    @Query("SELECT SUM(stockKg) FROM produits")
    fun stockTotal(): Flow<Double?>
}

@Database(entities = [Produit::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun produitDao(): ProduitDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun obtenir(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cooperative.db",
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}

/** Jeu de données initial, inséré au premier lancement. */
val produitsInitiaux = listOf(
    Produit(1, "Vanille Bourbon", "Sambava", 250_000.0, 18.5),
    Produit(2, "Café Arabica", "Itasy", 12_000.0, 42.0),
    Produit(3, "Girofle", "Analanjirofo", 38_000.0, 15.0),
    Produit(4, "Litchi", "Toamasina", null, 55.0),
    Produit(5, "Poivre noir", "Vatovavy", 45_000.0, 7.5),
)