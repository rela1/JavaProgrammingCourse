<%@ page import="java.lang.String, java.util.Random, java.awt.Color"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String bgColor = (String) request.getSession().getAttribute(
			"pickedBgCol");

	if (bgColor == null)
		bgColor = "WHITE";
%>

<%
	// kreiraj random boju sve dok ne dobiješ neku različitu od WHITE, CYAN, RED ili GREEN
	java.awt.Color color;
	do {
		Random randomGenerator = new Random();
		int red = randomGenerator.nextInt(255);
		int green = randomGenerator.nextInt(255);
		int blue = randomGenerator.nextInt(255);
		color = new Color(red, green, blue);
	} while (color.equals(Color.WHITE) || color.equals(Color.RED)
			|| color.equals(Color.CYAN) || color.equals(Color.GREEN));
	String hexColor = String.format("#%02x%02x%02x", color.getRed(),
			color.getGreen(), color.getBlue());
%>

<html>
<body bgcolor="<%=bgColor%>" text="<%=hexColor%>">
	<a href="../index.jsp">Index</a>
	<center>
		<br>
		<CENTER>
			<H3>CEO Party</H3>
		</CENTER>

		A CEO (and member of Forbes 400!) throwing a party takes his
		executives on a tour of his opulent mansion. In the back of the
		property, the CEO has the largest swimming pool any of them has ever
		seen. The huge pool, however, is filled with hungry alligators. The
		CEO says to his executives "I think an executive should be measured by
		courage. Courage is what made me CEO. So this is my challenge to each
		of you: if anyone has enough courage to dive into the pool, swim
		through those alligators, and make it to the other side, I will give
		that person anything they desire. My job, my money, my house,
		anything!"
		<P>Everyone laughs at the outrageous offer and proceeds to follow
			the CEO on the tour of the estate. Suddenly, they hear a loud splash.
			Everyone turns around and sees the CFO (Chief Financial Officer) in
			the pool, swimming for his life. He dodges the alligators left and
			right and makes it to the edge of the pool with seconds to spare. He
			pulls himself out just as a huge alligator snaps at his shoes. The
			flabbergasted CEO approaches the CFO and says, "You are amazing. I've
			never seen anything like it in my life. You are brave beyond measure
			and anything I own is yours. Tell me what I can do for you."
		<P>The CFO, panting for breath, looks up and says, "You can tell
			me who the hell pushed me in the pool!!"
		<P>
	</center>
	<center>
		<br>
		<CENTER>
			<H3>A Lawyer's Question</H3>
		</CENTER>

		A small town prosecuting attorney called his first witness to the
		stand in a trial--a grandmotherly, elderly woman. He approached her
		and asked, "Mrs. Jones, do you know me?"
		<P>She responded, "Why, yes, I do know you Mr. Williams. I've
			known you since you were a young boy. And frankly, you've been a big
			disappointment to me. You lie, you cheat on your wife, you manipulate
			people and talk about them behind their backs. You think you're a
			rising big shot when you haven't the brains to realize you never will
			amount to anything more than a two-bit paper pusher. Yes, I know
			you."
		<P>The lawyer was stunned. Not knowing what else to do he pointed
			across the room and asked, "Mrs. Williams, do you know the defense
			attorney?"
		<P>She again replied, "Why, yes I do. I've known Mr. Bradley since
			he was a youngster, too. I used to baby-sit him for his parents. And
			he, too, has been a real disappointment to me. He's lazy, bigoted, he
			has a drinking problem. The man can't build a normal relationship
			with anyone and his law practice is one of the shoddiest in the
			entire state. Yes, I know him."
		<P>At this point, the judge rapped the courtroom to silence and
			called both counselors to the bench. In a very quiet voice, he said
			with menace, "If either of you asks her if she knows me, you'll be in
			jail for contempt within 5 minutes!"
		<P>
	</center>
	<center>
		<br>
		<CENTER>
			<H3>A Medical Problem</H3>
		</CENTER>

		An old woman came into her doctor's office and confessed to an
		embarrassing problem. "I fart all the time, Doctor Johnson, but
		they're soundless, and they have no odor. In fact, since I've been
		here, I've farted no less than twenty times. What can I do?"
		<P>"Here's a prescription, Mrs. Harris. Take these pills three
			times a day for seven days and come back and see me in a week."
		<P>Next week an upset Mrs. Harris marched into Dr. Johnson's
			office. "Doctor, I don't know what was in those pills, but the
			problem is worse! I'm farting just as much, but now they smell
			terrible! What do you have to say for yourself?"
		<P>"Calm down, Mrs. Harris," said the doctor soothingly. "Now that
			we've fixed your sinuses, we'll work on your hearing!!!"
		<P>
	</center>
	<br>
	<a href="../index.jsp">Index</a>
</body>
</html>