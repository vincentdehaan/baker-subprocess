package nl.vindh.baker

import com.ing.baker.compiler.RecipeCompiler
import com.ing.baker.recipe.scaladsl.{Event, Ingredient, Interaction, Recipe}

object SubprocessApp extends App with WebshopConcepts with Subprocesses {
  // We define several subprocesses
  val manufactureSubprocess: Recipe =
    Recipe("ManufactureSubprocess")
      .withInteraction(manufactureGoods
        .withRequiredEvents(valid, paymentMade))

  val retrieveSubprocess: Recipe =
    Recipe("RetrieveSubprocess")
    .withInteraction(retrieveGoods
      .withRequiredEvents(valid, paymentMade))

  val webShopRecipe: Recipe =
    Recipe("WebShop")
      .withInteractions(
        validateOrder,
        shipGoods,
        sendInvoice
          .withRequiredEvent(goodsShipped)
      )
      .withSensoryEvents(
        customerInfoReceived,
        orderPlaced,
        paymentMade)

  val webshopWithFactory = webShopRecipe.withSubprocess(manufactureSubprocess)
  val webshopWithWarehouse = webShopRecipe.withSubprocess(retrieveSubprocess)

  val compiledWebshopWithFactory = RecipeCompiler.compileRecipe(webshopWithFactory)
  val compiledWebshopWithWarehouse = RecipeCompiler.compileRecipe(webshopWithWarehouse)

  println(compiledWebshopWithFactory.getRecipeVisualization)
  println(compiledWebshopWithWarehouse.getRecipeVisualization)
 }
