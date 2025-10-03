import arrow.core.Either
import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.domain.models.Rate
import com.bitpanda.livechallenge.domain.usecases.AssetsUseCase
import com.bitpanda.livechallenge.domain.usecases.RatesUseCase
import com.bitpanda.livechallenge.ui.coins.CoinUIModel
import com.bitpanda.livechallenge.ui.coins.CoinUiMapper
import com.bitpanda.livechallenge.ui.coins.CoinsContract
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent.OnBottomCoinsClick
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent.OnPullToRefresh
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent.OnRetryButtonClicked
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent.OnTopCoinsClick
import com.bitpanda.livechallenge.ui.coins.CoinsViewModel
import com.bitpanda.livechallenge.ui.coins.ScreenState.LOADING
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoinsViewModelTest {

    private val assetsUseCase: AssetsUseCase = mockk()
    private val ratesUseCase: RatesUseCase = mockk()
    private val coinUiMapper: CoinUiMapper = mockk()
    private lateinit var viewModel: CoinsViewModel

    private val initialState = CoinsContract.State(
        screenState = LOADING,
        isTop = true,
        coins = null
    )

    private val assets = listOf(
        Asset(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "BTC",
            rank = 1,
            price = 3000.0,
            changePercent24Hr = 3.11
        )
    )

    private val rates = listOf(
        Rate(
            id = "euro",
            symbol = "EUR",
            currencySymbol = "€",
            type = "fiat",
            rateUsd = "1.1716"
        )
    )

    private val coinsUiModel = listOf(
        CoinUIModel(
            id = "bitcoin",
            name = "Bitcoin",
            symbol = "BTC",
            price = "2000.0",
            rank = 1,
            changePercent24Hr = 3.11,
            priceDifference24Hr = "0.0"
        ),
        CoinUIModel(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            price = "1000.0",
            rank = 2,
            changePercent24Hr = 2.11,
            priceDifference24Hr = "0.0"
        )
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        every { coinUiMapper.map(any()) } returns coinsUiModel
        coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
        coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
        viewModel = CoinsViewModel(
            assetsUseCase = assetsUseCase,
            ratesUseCase = ratesUseCase,
            coinUiMapper = coinUiMapper
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state WHEN viewModel is created THEN state is loading`() =
        assertEquals(initialState, viewModel.state)

    /*@Test
    fun `GIVEN assets or rates are null WHEN load THEN state is ERROR`() = runTest {
        coEvery { assetsUseCase.invoke() } returns Either.Left(AppError.Connectivity)
        coEvery { ratesUseCase.invoke() } returns Either.Left(AppError.Connectivity)
        viewModel = CoinsViewModel(
            assetsUseCase = assetsUseCase,
            ratesUseCase = ratesUseCase,
            coinUiMapper = coinUiMapper,
        )
        advanceUntilIdle()
        assertEquals(ScreenState.ERROR, viewModel.state.screenState)
    }*/

    @Test
    fun `GIVEN OnTopCoinsClick WHEN onUiEvent THEN isTop true and coins sorted descending`() =
        runTest {
            advanceUntilIdle()
            viewModel.onUiEvent(OnTopCoinsClick)
            assertEquals(true, viewModel.state.isTop)
            assertEquals(
                coinsUiModel.sortedByDescending { it.changePercent24Hr },
                viewModel.state.coins
            )
        }

    @Test
    fun `GIVEN OnBottomCoinsClick WHEN onUiEvent THEN isTop false and coins sorted ascending`() =
        runTest {
            coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
            coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
            advanceUntilIdle()
            viewModel.onUiEvent(OnBottomCoinsClick)
            assertEquals(false, viewModel.state.isTop)
            assertEquals(coinsUiModel.sortedBy { it.changePercent24Hr }, viewModel.state.coins)
        }

    @Test
    fun `GIVEN OnPullToRefresh WHEN onUiEvent THEN state is loading and isRefreshing true`() =
        runTest {
            advanceUntilIdle()
            viewModel.onUiEvent(OnPullToRefresh)
            assertEquals(LOADING, viewModel.state.screenState)
            assertEquals(true, viewModel.state.isRefreshing)
        }

    @Test
    fun `GIVEN OnRetryButtonClicked WHEN onUiEvent THEN state is loading and isRefreshing false`() =
        runTest {
            coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
            coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
            advanceUntilIdle()
            viewModel.onUiEvent(OnRetryButtonClicked)
            assertEquals(LOADING, viewModel.state.screenState)
            assertEquals(false, viewModel.state.isRefreshing)
        }

    @Test
    fun `GIVEN assets and rates WHEN mapCoinsToEur THEN prices are converted`() = runTest {
        coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
        coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
        assertEquals(coinsUiModel.first().price, viewModel.state.coins?.first()?.price)
    }

    @Test
    fun `GIVEN coins WHEN getCoins THEN returns sorted and limited list`() = runTest {
        coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
        coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
        advanceUntilIdle()
        assertEquals(
            coinsUiModel.sortedByDescending { it.changePercent24Hr }.take(10),
            viewModel.state.coins?.takeLast(10)
        )
        viewModel.onUiEvent(OnBottomCoinsClick)
        assertEquals(
            coinsUiModel.sortedBy { it.changePercent24Hr }.take(10),
            viewModel.state.coins?.take(10)
        )
    }
}
