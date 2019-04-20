package nl.vindh.baker

import com.ing.baker.recipe.scaladsl.Recipe

trait Subprocesses {
  implicit class RecipeWithSubprocesses(r: Recipe) {
    def withSubprocess(sub: Recipe): Recipe =
      r.copy(
        interactions = r.interactions ++ sub.interactions,
        sensoryEvents = r.sensoryEvents ++ sub.sensoryEvents)
  }
}
