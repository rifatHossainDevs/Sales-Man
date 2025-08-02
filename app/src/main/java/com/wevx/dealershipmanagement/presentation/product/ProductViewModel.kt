import androidx.lifecycle.ViewModel
import com.wevx.dealershipmanagement.domain.models.ProductModel

class ProductViewModel : ViewModel() {
    var allProducts: List<ProductModel> = listOf()
    var filteredProducts: List<ProductModel> = listOf()
}
