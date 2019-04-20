package nl.vindh.baker

import com.ing.baker.compiler.RecipeCompiler
import com.ing.baker.recipe.scaladsl.{Event, Ingredient, Interaction, Recipe}

object SubprocessApp extends App with WebshopConcepts with Subprocesses {
  // We define several subprocesses
  val generalGoodAcquisitionProcess = ExpandableSubprocess(
    name = "GeneralGoodAcquisitionProcess",
    inputIngredients = Seq(order),
    outputIngredients = Seq(goods)
  )

  val manufactureSubprocess: Recipe =
    Recipe("ManufactureSubprocess")
      .withInteraction(manufactureGoods)

  val retrieveSubprocess: Recipe =
    Recipe("RetrieveSubprocess")
    .withInteraction(retrieveGoods)

  val webShopRecipe: Recipe =
    Recipe("WebShop")
      .withInteractions(
        validateOrder,
        shipGoods,
        sendInvoice
          .withRequiredEvent(goodsShipped),
        generalGoodAcquisitionProcess
          .withRequiredEvents(valid, paymentMade)
      )
      .withSensoryEvents(
        customerInfoReceived,
        orderPlaced,
        paymentMade)

  val webshopWithFactory = webShopRecipe.expandSubprocess(generalGoodAcquisitionProcess, manufactureSubprocess)
  val webshopWithWarehouse = webShopRecipe.expandSubprocess(generalGoodAcquisitionProcess, retrieveSubprocess)

  val compiledWebshop = RecipeCompiler.compileRecipe(webShopRecipe)
  val compiledWebshopWithFactory = RecipeCompiler.compileRecipe(webshopWithFactory)
  val compiledWebshopWithWarehouse = RecipeCompiler.compileRecipe(webshopWithWarehouse)

  println(compiledWebshop.getRecipeVisualization)
  println(compiledWebshopWithFactory.getRecipeVisualization)
  println(compiledWebshopWithWarehouse.getRecipeVisualization)
 }
