package nl.vindh.baker

import com.ing.baker.recipe.scaladsl.{Event, Ingredient, Interaction}

trait WebshopConcepts {
  // Adapted from /recipe-dsl/src/test/scala/com/ing/baker/recipe/scaladsl/Examples.scala
  case class CustomerInfo(name: String, address: String, email: String)

  // ingredients

  val customerInfo = Ingredient[CustomerInfo]("customerInfo")
  val goods = Ingredient[String]("goods")
  val trackingId = Ingredient[String]("trackingId")
  val order = Ingredient[String]("order")
  val name = Ingredient[String]("name")
  val address = Ingredient[String]("address")
  val email = Ingredient[String]("email")

  // events

  val goodsShipped = Event("GoodsShipped", trackingId)
  val orderPlaced = Event("OrderPlaced", order)
  val customer = Event("Customer", name, address, email)
  val customerInfoReceived = Event("CustomerInfoReceived", customerInfo)
  val paymentMade = Event("PaymentMade")
  val valid = Event("Valid")
  val sorry = Event("Sorry")
  val goodsManufactured = Event("GoodsManufactured", goods)
  val goodsRetrieved = Event("GoodsRetrieved", goods)
  val invoiceWasSent = Event("InvoiceWasSent")

  // interactions

  val validateOrder = Interaction(
    name = "ValidateOrder",
    inputIngredients = Seq(order),
    output = Seq(valid, sorry))

  val manufactureGoods = Interaction(
    name = "ManufactureGoods",
    inputIngredients = Seq(order),
    output = Seq(goodsManufactured))

  val retrieveGoods = Interaction(
    name = "RetrieveGoods",
    inputIngredients = Seq(order),
    output = Seq(goodsRetrieved))

  val sendInvoice = Interaction(
    name = "SendInvoice",
    inputIngredients = Seq(customerInfo),
    output = Seq(invoiceWasSent))

  val shipGoods = Interaction(
    name = "ShipGoods",
    inputIngredients = Seq(goods, customerInfo),
    output = Seq(goodsShipped)
  )

}
