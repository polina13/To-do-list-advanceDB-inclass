import org.fluentlenium.adapter.FluentTest;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.assertj.core.api.Assertions.*;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
      return webDriver;
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @ClassRule
  public static ServerRule server = new ServerRule();


  @Test
  public void rootTest() {
      goTo("http://localhost:4567/");
      assertThat(pageSource()).contains("Welcome to To Do List");
  }

  @Test
    public void categoryIsCreatedTest() {
      goTo("http://localhost:4567/");
      Category myCategory = new Category("Household chores");
      myCategory.save();
      goTo("http://localhost:4567/categories");
      assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void categoryIsDisplayedTest() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    String categoryPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
    goTo(categoryPath);
    assertThat(pageSource()).contains("Household chores");
  }


  @Test
  public void allTasksDescriptionOnCategoryPage() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Task firstTask = new Task("Mow the lawn", false);
    firstTask.save();
    Task secondTask = new Task("Do the dishes", false);
    secondTask.save();
    String categoryPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
    goTo(categoryPath);
    assertThat(pageSource()).contains("Mow the lawn");
    assertThat(pageSource()).contains("Do the dishes");
  }
}
