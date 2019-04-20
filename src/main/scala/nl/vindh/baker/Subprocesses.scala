package nl.vindh.baker

import com.ing.baker.recipe.common.Ingredient
import com.ing.baker.recipe.scaladsl.{Event, Interaction, Recipe}

trait Subprocesses {
  object ExpandableSubprocess {
    def apply(name: String, inputIngredients: Seq[Ingredient], outputIngredients: Seq[Ingredient]): Interaction with ExpandableSubprocess =
      Interaction(name = name, inputIngredients = inputIngredients, output = Seq(DummyEvent(name, outputIngredients)))
        .asInstanceOf[Interaction with ExpandableSubprocess]
  }

  class ExpandableSubprocess

  object DummyEvent {
    def apply(name: String, outputIngredients: Seq[Ingredient]): Event =
      Event(s"DummyEvent#$name", outputIngredients:_*)
  }

  implicit class RecipeWithSubprocesses(r: Recipe) {
    // TODO: decide how to merge the general properties of the subprocess and the main process
    def expandSubprocess(expandable: Interaction with ExpandableSubprocess, sub: Recipe): Recipe = {
      val expandableFinal = r.interactions.find(_.name == expandable.name).getOrElse(expandable)
      r.copy(
        interactions = r.interactions.filterNot(_.name == expandable.name) ++ sub.interactions.map {
          interaction =>
            if(interaction.requiredEvents.size == 0)
              interaction.copy(requiredEvents = expandableFinal.requiredEvents)
            else interaction
        },
        sensoryEvents = r.sensoryEvents ++ sub.sensoryEvents
      )
    }
  }
}
