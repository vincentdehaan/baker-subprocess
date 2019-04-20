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

  val eventA = Event("Event A")
  val eventB = Event("Event B")
  val eventC = Event("Event C")
  val eventD = Event("Event D", goods)
  val intA = Interaction("Interaction A", Seq(), Seq(eventA))
  val intB = Interaction("Interaction B", Seq(), Seq(eventB))
  val intC = Interaction("Interaction C", Seq(), Seq(eventC))
  val intD = Interaction("Interaction D", Seq(), Seq(eventD))

  val complicatedSubprocess: Recipe =
    Recipe("ComplicatedSubprocess")
    .withInteractions(
      intA,
      intB,
      intC,
      intD.withRequiredEvents(eventA, eventB))

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
  val complicatedWebshop = webShopRecipe.expandSubprocess(generalGoodAcquisitionProcess, complicatedSubprocess)

  val compiledWebshop = RecipeCompiler.compileRecipe(webShopRecipe)
  val compiledWebshopWithFactory = RecipeCompiler.compileRecipe(webshopWithFactory)
  val compiledWebshopWithWarehouse = RecipeCompiler.compileRecipe(webshopWithWarehouse)
  val compiledComplicatedWebshop = RecipeCompiler.compileRecipe(complicatedWebshop)

  println(compiledWebshop.getRecipeVisualization)
  println(compiledWebshopWithFactory.getRecipeVisualization)
  println(compiledWebshopWithWarehouse.getRecipeVisualization)
  println(compiledComplicatedWebshop.getRecipeVisualization)
 }
