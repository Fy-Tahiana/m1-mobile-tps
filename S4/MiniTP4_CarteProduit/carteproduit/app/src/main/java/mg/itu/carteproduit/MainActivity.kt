package mg.itu.carteproduit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Produit(
    val nom: String,
    val origine: String,
    val prixKg: Double?,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    ProduitCard(Produit("Vanille Bourbon", "Sambava", 250_000.0))
                }
            }
        }
    }
}

@Composable
fun ProduitCard(produit: Produit) {
    Log.i("RECOMP", "ProduitCard se (re)compose")

    var quantite by remember { mutableStateOf(0) }          // TODO A — état
    var selectionnee by remember { mutableStateOf(false) }  // TODO B — état

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { selectionnee = !selectionnee },    // TODO B — clic
        colors = CardDefaults.cardColors(                   // TODO B — couleur
            containerColor = if (selectionnee)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        ),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(produit.nom, style = MaterialTheme.typography.titleLarge)
            Text("Origine : ${produit.origine}", style = MaterialTheme.typography.bodyMedium)
            Text(
                produit.prixKg?.let { "${formatAriary(it)} / kg" } ?: "prix non fixé",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(12.dp))
            Text("Quantité : $quantite kg")                  // TODO A — affichage
            Button(onClick = { quantite++ }) { Text("Ajouter 1 kg") }  // TODO A — bouton
        }
    }
}

fun formatAriary(montant: Double): String {
    val entier = montant.toLong().toString()
    val groupes = entier.reversed().chunked(3).joinToString(" ").reversed()
    return "$groupes Ar"
}