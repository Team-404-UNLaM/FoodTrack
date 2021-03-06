package com.team404.foodtrack.ui.market.favs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import coil.compose.AsyncImage
import com.google.gson.GsonBuilder
import com.team404.foodtrack.R
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.MarketAddress
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.domain.factories.FavsMarketsViewModelFactory
import com.team404.foodtrack.domain.repositories.MarketFavoritesRepository
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.ui.market.favs.ui.theme.DefaultGray
import com.team404.foodtrack.ui.market.favs.ui.theme.RedLetter
import com.team404.foodtrack.ui.market.favs.ui.theme.Yellow
import com.team404.foodtrack.utils.transformToLowercaseAndReplaceSpaceWithDash
import org.koin.android.ext.android.inject

class FavFragment : Fragment() {
    private lateinit var viewModel : FavsViewModel
    private lateinit var factory: FavsMarketsViewModelFactory
    private val marketRepository : MarketRepository by inject()
    private lateinit var  marketFavoritesRepository : MarketFavoritesRepository
    private lateinit var room: FoodTrackDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        room = FoodTrackDB.getDatabase(requireContext())
        marketFavoritesRepository = MarketFavoritesRepository(room.marketFavoritesDao())
        factory = FavsMarketsViewModelFactory(marketFavoritesRepository)
        viewModel = ViewModelProvider(this, factory).get(FavsViewModel::class.java)

       return ComposeView(requireContext()).apply{
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Surface(modifier = Modifier.fillMaxSize()){
                        FavsScreenContent()
                    }
                }
            }
        }
    }

    @Composable
    private fun FavsScreenContent() {
        BackgroundImage()
        val items = viewModel.marketsFavorites.value
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        if (items.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchView(textState)
                FavComponents(items, textState)
            }
        } else {
            NoMarketsFavorites()
        }
    }

    @Composable
    private fun BackgroundImage(){
        Image(
            painter = painterResource(R.drawable.food_background),
            contentDescription = "App background image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    @Composable
    private fun SearchView(textState: MutableState<TextFieldValue>) {
        TextField(
            value = textState.value,
            onValueChange = {value ->
                textState.value = value
            },
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(start = 15.dp, top = 15.dp, end = 15.dp, bottom = 15.dp),
            placeholder = {Text(text = "Buscar local", color = Color.Black)},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search icon"
                )
            },
            trailingIcon = {
                if(textState.value != TextFieldValue("")){
                    IconButton(
                        onClick = {
                            textState.value = TextFieldValue("")
                        }
                    ){
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "icon close"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                cursorColor = Color.Black,
                leadingIconColor = Color.Black,
                trailingIconColor = Color.Black,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun FavComponents(data: List<MarketFavorites>, textState: MutableState<TextFieldValue>) {
        val markets = data
        var filteredMarkets : List<MarketFavorites>

        androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalArrangement = Arrangement.Center,
        ) {
            val searchedText = transformToLowercaseAndReplaceSpaceWithDash(textState.value.text)
            filteredMarkets = if(searchedText.isEmpty()){
                markets
            }else{
                val resultList = ArrayList<MarketFavorites>()
                for(market in markets){
                    if(transformToLowercaseAndReplaceSpaceWithDash(market.name).contains(searchedText)){
                        resultList.add(market)
                    }
                }
                resultList
            }
            items(filteredMarkets) { marketItem ->
                MarketItem(marketItem)
            }
        }
    }

    @Composable
    private fun MarketItem(marketFavorite: MarketFavorites){
        val market = marketRepository.searchById(marketFavorite.marketId)
        CustomFavItem(
            marketId = market.id,
            marketName = market.name ?: "",
            imageUrl = market.marketImg,
            address = marketAddressToString(market.address),
            market = marketFavorite
        )
    }

    private fun marketAddressToString(address: MarketAddress?): String {
        var marketAddress = ""
        if(address != null){
            marketAddress = "${address.street} ${address.number}, ${address.city}"
        }
        return marketAddress
    }

    @Composable
    private fun CustomFavItem(
        marketName: String = "",
        imageUrl: String?,
        address: String = "",
        marketId: Long?,
        market: MarketFavorites
    ){
        Card(
            modifier = Modifier.padding(start = 4.dp,bottom = 16.dp, end = 4.dp),
            shape = RoundedCornerShape(30.dp),
            backgroundColor = Color.White,
            border = BorderStroke(1.dp, Color.Black)
        ){

            var expandable by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.clickable { expandable = !expandable }
            ){
                IconButton(
                    onClick={
                        viewModel.deleteMarketFromFavorites(market)
                        expandable = false
                    }){
                    Icon(modifier = Modifier.padding(top = 15.dp, start = 15.dp).align(Alignment.Start),
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "market favorite icon",
                        tint = Color.Red
                    )
                }
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Market image",
                    placeholder = painterResource(R.drawable.ic_market),
                    error = painterResource(R.drawable.ic_market),
                    alignment = Alignment.Center,
                    modifier = Modifier.size(110.dp).padding(top = 0.dp, start= 15.dp, end= 15.dp).align(Alignment.CenterHorizontally)
                )
                Text(
                    modifier = Modifier.wrapContentWidth().padding(top = 5.dp, bottom = 10.dp, start= 15.dp, end = 15.dp).align(Alignment.CenterHorizontally),
                    text = marketName,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    overflow = if (expandable) {
                        TextOverflow.Visible
                    } else {
                        TextOverflow.Ellipsis
                    },
                    maxLines = if (expandable) 4 else 1

                )
                AnimatedVisibility(expandable){
                    Column(
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    ){
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = address,
                                color = DefaultGray
                            )
                        ClickableText(
                            modifier = Modifier.align(alignment = Alignment.Start).padding(top = 10.dp, bottom = 15.dp),
                            text = AnnotatedString(text = "Hacer pedido"),
                            style = TextStyle(textDecoration = TextDecoration.None, color = RedLetter),
                            onClick = {
                                navigateToMakeOrder(marketId)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun navigateToMakeOrder(marketId: Long?) {
        val order = Order.Builder().marketId(marketId!!)
        val bundle = Bundle()
        bundle.putString("order", GsonBuilder().create().toJson(order))
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_favFragment_to_selectConsumptionModeFragment, bundle)
        }
    }

    @Composable
    private fun NoMarketsFavorites(){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.food_image),
                contentDescription = "food icon")
            Text(
                text = "A??n no ten??s ning??n local favorito",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold
            )
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.updateMarketsFavorites()
    }
}