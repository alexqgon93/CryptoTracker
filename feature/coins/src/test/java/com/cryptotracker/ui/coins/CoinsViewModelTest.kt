import arrow.core.Either
import com.cryptotracker.domain.models.AppError
import com.cryptotracker.domain.models.Asset
import com.cryptotracker.domain.models.Rate
import com.cryptotracker.domain.usecases.AssetsUseCase
import com.cryptotracker.domain.usecases.RatesUseCase
import com.cryptotracker.ui.coins.CoinUIModel
import com.cryptotracker.ui.coins.CoinUiMapper
import com.cryptotracker.ui.coins.CoinsContract
import com.cryptotracker.ui.coins.CoinsContract.UiEvent.OnBottomCoinsClick
import com.cryptotracker.ui.coins.CoinsContract.UiEvent.OnPullToRefresh
import com.cryptotracker.ui.coins.CoinsContract.UiEvent.OnRetryButtonClicked
import com.cryptotracker.ui.coins.CoinsContract.UiEvent.OnTopCoinsClick
import com.cryptotracker.ui.coins.CoinsViewModel
import com.cryptotracker.ui.coins.ScreenState.ERROR
import com.cryptotracker.ui.coins.ScreenState.LOADING
import com.cryptotracker.ui.coins.ScreenState.SUCCESS
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CoinsViewModelTest {
    private val assetsUseCase: AssetsUseCase = mockk()
    private val ratesUseCase: RatesUseCase = mockk()
    private val coinUiMapper: CoinUiMapper = mockk()
    private lateinit var viewModel: CoinsViewModel
    private val dispatcher = StandardTestDispatcher()

    private val initialState =
        CoinsContract.State(
            screenState = LOADING,
            isTop = true,
            coins = null,
        )

    private val assets =
        listOf(
            Asset(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                rank = 1,
                price = 3000.0,
                changePercent24Hr = 3.11,
            ),
        )

    private val rates =
        listOf(
            Rate(
                id = "euro",
                symbol = "EUR",
                currencySymbol = "€",
                type = "fiat",
                rateUsd = "1.1716",
            ),
        )

    private val coinsUiModel =
        listOf(
            CoinUIModel(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                price = "2000.0",
                rank = 1,
                changePercent24Hr = 3.11,
                priceDifference24Hr = "0.0",
            ),
            CoinUIModel(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                price = "1000.0",
                rank = 2,
                changePercent24Hr = 2.11,
                priceDifference24Hr = "0.0",
            ),
        )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        every { coinUiMapper.map(any()) } returns coinsUiModel
        coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
        coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
        viewModel =
            CoinsViewModel(
                assetsUseCase = assetsUseCase,
                ratesUseCase = ratesUseCase,
                coinUiMapper = coinUiMapper,
                ioDispatcher = dispatcher,
            )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state WHEN viewModel is created THEN state is loading`() = assertEquals(initialState, viewModel.state.value)

    @Test
    fun `GIVEN OnTopCoinsClick WHEN onUiEvent THEN isTop true and coins sorted descending`() =
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.onUiEvent(OnTopCoinsClick)
            assertEquals(true, viewModel.state.value.isTop)
            assertEquals(
                coinsUiModel.sortedByDescending { it.changePercent24Hr },
                viewModel.state.value.coins,
            )
        }

    @Test
    fun `GIVEN OnBottomCoinsClick WHEN onUiEvent THEN isTop false and coins sorted ascending`() =
        runTest {
            coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
            coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.onUiEvent(OnBottomCoinsClick)
            assertEquals(false, viewModel.state.value.isTop)
            assertEquals(coinsUiModel.sortedBy { it.changePercent24Hr }, viewModel.state.value.coins)
        }

    @Test
    fun `GIVEN OnPullToRefresh WHEN loading completes THEN state is success and isRefreshing false`() =
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.onUiEvent(OnPullToRefresh)
            dispatcher.scheduler.advanceUntilIdle()
            assertEquals(SUCCESS, viewModel.state.value.screenState)
            assertEquals(false, viewModel.state.value.isRefreshing)
        }

    @Test
    fun `GIVEN OnRetryButtonClicked WHEN loading completes THEN state is success and isRefreshing false`() =
        runTest {
            coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
            coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.onUiEvent(OnRetryButtonClicked)
            dispatcher.scheduler.advanceUntilIdle()
            assertEquals(SUCCESS, viewModel.state.value.screenState)
            assertEquals(false, viewModel.state.value.isRefreshing)
        }

    @Test
    fun `GIVEN assets and rates WHEN mapCoinsToEur THEN prices are converted`() =
        runTest {
            coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
            coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
            dispatcher.scheduler.advanceUntilIdle()
            assertEquals(
                coinsUiModel.first().price,
                viewModel.state.value.coins
                    ?.first()
                    ?.price,
            )
        }

    @Test
    fun `GIVEN coins WHEN getCoins THEN returns sorted and limited list`() =
        runTest {
            coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
            coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
            dispatcher.scheduler.advanceUntilIdle()
            assertEquals(
                coinsUiModel.sortedByDescending { it.changePercent24Hr }.take(10),
                viewModel.state.value.coins
                    ?.takeLast(10),
            )
            viewModel.onUiEvent(OnBottomCoinsClick)
            assertEquals(
                coinsUiModel.sortedBy { it.changePercent24Hr }.take(10),
                viewModel.state.value.coins
                    ?.take(10),
            )
        }

    @Test
    fun `GIVEN bottom mode WHEN refresh completes THEN bottom mode is preserved`() =
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.onUiEvent(OnBottomCoinsClick)
            viewModel.onUiEvent(OnPullToRefresh)
            dispatcher.scheduler.advanceUntilIdle()
            assertEquals(false, viewModel.state.value.isTop)
            assertEquals(coinsUiModel.sortedBy { it.changePercent24Hr }, viewModel.state.value.coins)
        }

    @Test
    fun `GIVEN bottom mode WHEN retry completes THEN bottom mode is preserved`() =
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            viewModel.onUiEvent(OnBottomCoinsClick)

            viewModel.onUiEvent(OnRetryButtonClicked)
            dispatcher.scheduler.advanceUntilIdle()

            assertEquals(false, viewModel.state.value.isTop)
            assertEquals(coinsUiModel.sortedBy { it.changePercent24Hr }, viewModel.state.value.coins)
        }

    @Test
    fun `GIVEN rates without euro WHEN loading completes THEN asset prices are not converted`() =
        runTest {
            val mappedAssets = slot<List<Asset>>()
            coEvery { ratesUseCase.invoke() } returns
                Either.Right(
                    listOf(
                        Rate(
                            id = "united-states-dollar",
                            symbol = "USD",
                            currencySymbol = "$",
                            type = "fiat",
                            rateUsd = "1.0",
                        ),
                    ),
                )
            every { coinUiMapper.map(capture(mappedAssets)) } returns coinsUiModel
            viewModel =
                CoinsViewModel(
                    assetsUseCase = assetsUseCase,
                    ratesUseCase = ratesUseCase,
                    coinUiMapper = coinUiMapper,
                    ioDispatcher = dispatcher,
                )

            dispatcher.scheduler.advanceUntilIdle()

            assertEquals(SUCCESS, viewModel.state.value.screenState)
            assertEquals(assets.first().price, mappedAssets.captured.first().price)
        }

    @Test
    fun `GIVEN app error WHEN loading completes THEN error state contains mapped message`() =
        runTest {
            val exception = IOException("Network unavailable")
            coEvery { assetsUseCase.invoke() } returns Either.Left(AppError.NetworkError(exception))
            coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
            viewModel =
                CoinsViewModel(
                    assetsUseCase = assetsUseCase,
                    ratesUseCase = ratesUseCase,
                    coinUiMapper = coinUiMapper,
                    ioDispatcher = dispatcher,
                )

            dispatcher.scheduler.advanceUntilIdle()

            assertEquals(ERROR, viewModel.state.value.screenState)
            assertEquals("Network unavailable", viewModel.state.value.errorMessage)
        }

    @Test
    fun `GIVEN domain error with message WHEN loading completes THEN error state surfaces message`() =
        runTest {
            coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
            coEvery { ratesUseCase.invoke() } returns Either.Left(AppError.BadRequest("Invalid rates payload"))
            viewModel =
                CoinsViewModel(
                    assetsUseCase = assetsUseCase,
                    ratesUseCase = ratesUseCase,
                    coinUiMapper = coinUiMapper,
                    ioDispatcher = dispatcher,
                )

            dispatcher.scheduler.advanceUntilIdle()

            assertEquals(ERROR, viewModel.state.value.screenState)
            assertEquals("Invalid rates payload", viewModel.state.value.errorMessage)
        }

    @Test
    fun `GIVEN refresh fails after success WHEN loading completes THEN coins remain visible with error message`() =
        runTest {
            dispatcher.scheduler.advanceUntilIdle()
            coEvery { assetsUseCase.invoke() } returns Either.Left(AppError.Server)
            coEvery { ratesUseCase.invoke() } returns Either.Right(rates)

            viewModel.onUiEvent(OnPullToRefresh)
            dispatcher.scheduler.advanceUntilIdle()

            assertEquals(SUCCESS, viewModel.state.value.screenState)
            assertEquals(false, viewModel.state.value.isRefreshing)
            assertEquals(coinsUiModel.sortedByDescending { it.changePercent24Hr }, viewModel.state.value.coins)
            assertEquals("The price service is not responding. Please try again later.", viewModel.state.value.errorMessage)
        }

    @Test
    fun `GIVEN mapper returns no coins WHEN loading completes THEN success state contains empty list`() =
        runTest {
            every { coinUiMapper.map(any()) } returns emptyList()
            viewModel =
                CoinsViewModel(
                    assetsUseCase = assetsUseCase,
                    ratesUseCase = ratesUseCase,
                    coinUiMapper = coinUiMapper,
                    ioDispatcher = dispatcher,
                )

            dispatcher.scheduler.advanceUntilIdle()

            assertEquals(SUCCESS, viewModel.state.value.screenState)
            assertEquals(emptyList<CoinUIModel>(), viewModel.state.value.coins)
        }
}
