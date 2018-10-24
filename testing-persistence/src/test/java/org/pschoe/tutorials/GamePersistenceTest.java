package org.pschoe.tutorials;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GamePersistenceTest {

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addPackage(Game.class.getPackage())
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	private static final String[] GAME_TITLES = { "Super Mario Brothers", "Mario Kart", "F-Zero" };

	@PersistenceContext
	EntityManager em;

	@Inject
	UserTransaction utx;

	
	@Before
	public void preparePersistenceTest() throws Exception {
	    clearData();
	    insertData();
	    startTransaction();
	}

	private void insertData() throws Exception {
		utx.begin();
		em.joinTransaction();
		System.out.println("Inserting records...");
		for (String title : GAME_TITLES) {
			Game game = new Game(title);
			em.persist(game);
		}
		utx.commit();
		// clear the persistence context (first-level cache)
		em.clear();
	}

	private void clearData() throws Exception {
		System.out.println("______________clearData________________");
		utx.begin();
		em.joinTransaction();
		System.out.println("Dumping old records...");
		em.createQuery("delete from Game").executeUpdate();
		utx.commit();
	}

	private void startTransaction() throws Exception {
		System.out.println("______________startTransaction_______________");
	    utx.begin();
	    em.joinTransaction();
	}

	private static void assertContainsAllGames(Collection<Game> games) {
		System.out.println("______________asserContainsAllGames_______________");
		Assert.assertEquals(GAME_TITLES.length, games.size());
		final Set<String> retrivedGameTitles = new HashSet<String>();
		for (Game game : games) {
			System.out.println("* " + game);
			retrivedGameTitles.add(game.getTitle());
		}

		Assert.assertTrue(retrivedGameTitles.containsAll(Arrays.asList(GAME_TITLES)));
	}

	@After
	public void commitTransaction() throws Exception {
		utx.commit();
	}

	@InSequence(1)
	@Test
	public void shouldFindAllGamesUsingJpqlQuery() throws Exception{
		System.out.println("______________JPQL_______________");
		String fetchingAllGamesInJpql = "select g from Game g order by g.id";

		System.out.println("führe Jpql Query aus...");
		List<Game> games = em.createQuery(fetchingAllGamesInJpql, Game.class).getResultList();

		System.out.println("Habe " + games.size() + " games gefunden. (JPQL)");
		assertContainsAllGames(games);
	}

	@InSequence(2)
	@Test
	public void shouldFindAllGamesUsingCriteriaApi() throws Exception {
		System.out.println("______________CriteriaAPI_______________");
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Game> criteria = builder.createQuery(Game.class);

		Root<Game> game = criteria.from(Game.class);
		criteria.select(game);


		System.out.println("führe Criteria Query aus...");
		List<Game> games = em.createQuery(criteria).getResultList();

		System.out.println("Habe " + games.size() + " games gefunden. (Criteria)");
		assertContainsAllGames(games);
	}
}
