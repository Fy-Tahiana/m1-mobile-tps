package mg.itu.listedetail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** L'état complet de l'interface, en une seule donnée immuable. */
data class EtatUi(
    val produits: List<Produit> = emptyList(),
    val poidsPanierKg: Int = 0,
)

class ProduitsViewModel : ViewModel() {

    // TODO 1 fait — le duo privé mutable / public lecture seule : flux unidirectionnel.
    private val _uiState = MutableStateFlow(EtatUi(produits = produits))
    val uiState: StateFlow<EtatUi> = _uiState

    /** Appelée par l'écran de détail quand l'utilisateur ajoute au panier. */
    fun ajouterAuPanier(poidsKg: Int) {
        // TODO 2 fait — l'événement fait évoluer l'état de façon immuable (copy, S1) :
        _uiState.update { etat ->
            etat.copy(poidsPanierKg = etat.poidsPanierKg + poidsKg)
        }
    }
}