package mg.itu.listedetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Mini-TP 7 — le ViewModel, désormais branché sur la base Room.
 *
 * Ce qui change par rapport à la séance 6 : l'état ne vient plus d'une liste
 * en mémoire, mais de la BASE — via des Flow qui ré-émettent à chaque
 * changement. La base est la source de vérité ; l'écran n'en est qu'un reflet.
 *
 * Rien à modifier ici tant que les TODO du DAO ne sont pas écrits.
 * Ensuite : suivez le bloc « ÉTAPE 3 » pour brancher vos requêtes.
 */

enum class ModeAffichage { NOM, PRIX_DECROISSANT, STOCK_SUFFISANT }

data class EtatUi(
    val produits: List<Produit> = emptyList(),
    val mode: ModeAffichage = ModeAffichage.NOM,
    val stockTotal: Double? = null,
)

class ProduitsViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.obtenir(application).produitDao()
    private val mode = MutableStateFlow(ModeAffichage.NOM)

    init {
        viewModelScope.launch {
            if (dao.parId(1) == null) dao.insererTous(produitsInitiaux)
        }
    }

    val uiState: StateFlow<EtatUi> =
        combine(
            dao.tousLesProduits(),
            dao.parPrixDecroissant(),        // TODO 1 branché
            dao.stockSuperieurA(10.0),       // TODO 2 branché
            dao.stockTotal(),                // TODO 3 branché
            mode,
        ) { parNom, parPrix, stockOk, total, modeCourant ->
            val liste = when (modeCourant) {
                ModeAffichage.NOM -> parNom
                ModeAffichage.PRIX_DECROISSANT -> parPrix
                ModeAffichage.STOCK_SUFFISANT -> stockOk
            }
            EtatUi(produits = liste, mode = modeCourant, stockTotal = total)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EtatUi(),
        )

    fun changerMode(nouveau: ModeAffichage) {
        mode.value = nouveau
    }
}
