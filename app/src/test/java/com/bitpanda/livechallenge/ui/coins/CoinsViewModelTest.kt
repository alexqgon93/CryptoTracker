import arrow.core.Either
import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.domain.models.Rate
import com.bitpanda.livechallenge.domain.usecases.AssetsUseCase
import com.bitpanda.livechallenge.domain.usecases.RatesUseCase
import com.bitpanda.livechallenge.ui.coins.CoinUIModel
import com.bitpanda.livechallenge.ui.coins.CoinUiMapper
import com.bitpanda.livechallenge.ui.coins.CoinsContract
import com.bitpanda.livechallenge.ui.coins.CoinsViewModel
import com.bitpanda.livechallenge.ui.coins.ScreenState.LOADING
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CoinsViewModelTest {

    private val assetsUseCase: AssetsUseCase = mockk()
    private val ratesUseCase: RatesUseCase = mockk()
    private val coinUiMapper: CoinUiMapper = mockk()
    private val viewModel: CoinsViewModel by lazy {
        CoinsViewModel(
            assetsUseCase,
            ratesUseCase,
            coinUiMapper
        )
    }

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
            price = "20000.0",
            rank = 1,
            changePercent24Hr = 3.11,
            priceDifference24Hr = "0.0"
        )
    )

    @BeforeEach
    fun setup() {
        every { coinUiMapper.map(any()) } returns coinsUiModel
        coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
        coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
    }

    @Test
    fun `GIVEN initial state WHEN viewModel is created THEN state is loading`() {
        assertEquals(initialState, viewModel.state)
    }

    @Test
    fun `GIVEN init  THEN state is SUCCESS and coins are set`() = runTest {
        coEvery { assetsUseCase.invoke() } returns Either.Right(assets)
        coEvery { ratesUseCase.invoke() } returns Either.Right(rates)
        assertEquals(LOADING, viewModel.state.screenState)
        assertEquals(null, viewModel.state.coins)
    }
}
