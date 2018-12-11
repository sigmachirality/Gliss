=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: dtao
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- How to play: Slide between the keys CAPS lock and ENTER on the keyboard. Hit the notes
for score!

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Collections: The game uses collections to store the Notes and Sliders parsed
  from the file containing the notes for a particular map. During gameplay the
  Level object programmatically calls the Notes that should appear on screen
  using a functional programming filter function, and draws every Note in the 
  resulting stream.

  2. File I/O: File I/O is present in two different parts of the game. First is 
  in storing the music levels: each level is stored in a text file, where each line
  represents either a Note or a Slider object. The other place where it is implemented
  is in the Highscore system, which also its information in a text file. If the text
  file does not exist, it is created. Whenever either parser encounters an invalid line,
  it skips it. This means that even if a user accidently adds a line to either file type,
  the game is still playable.

  3. Inheritance/Subtyping for Dynamic Dispatch: The Slider object is a child of the
  Note object. This means that I can store Slider objects in the same collection of
  Note objects as the notes, and that I can just call "draw" or "getScore" on the objects
  to calculate how to draw the object on screen/score the objects respectively even
  when notes and sliders are handled very differently by the game engine. The Note
  does not extend from the GameObj class - rather I had to build it from scratch to
  allow it to hook into the Clip class of Java's built in sound library.

  4. Concurrency: This game features concurrency to allow the game to run faster.
  It is extremely important in Rhythm games that the gameplay does not desync from
  the music. Because of this, I am actually using the timestamp given by the Clip.
  getmicrosecondPosition class instead of the built in Java timer to calculate note
  position and score, because this is obviously more accurate to the music than the
  built in timer. There are three threads in this game - GameCourt, Level, and Keyboard.
  Keyboard and Level both draw to bufferedImage objects which are then composited on
  top of each other and displayed to the screen. This ensures the smoothest
  gameplay experience possible.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

- Audio - This class manages the audio files in the game. Calling .play(int) allows
you to select a particular audio file by index (the order in which the sound was added).
This allows different threads to call for a sound to be played, which is important, 
especially with the three threads that I'm working with. 

- Converter - This class is not actually used in the game. I used it to convert
osu! beatmaps into a file format readable by my game. Code from this class was
later resused in creating the highscore functionality of the Level class.

- Game - This class just sets up the game and then class GameCourt to actually start 
the game.

- GameCourt - This class houses the other objects and initializes them. It also is
responsible for compositing the different bufferedImages the different classes 
produce into a frame to be displayed to the screen, and then displaying it. It does
this by taking all objects in a concurrentDeque and the compositing them into
one Image, and then displaying it in the court. This ensures that a image is
displayed at all times. Since Keyboard and Level only input into this Deque,
and this class only clears the deque after the existing bufferedImages in the Deque
have been displayed, there is not much possibility for concurrency memory management
problems.

- Keyboard - This class tracks which keys are pressed and determines whether Note
objects have been hit using the Shape intersect method when Note objects are passed in.
The Keyboard also is a thread outputing BufferedImages into a concurrentDeque where they
are stored until GameCourt composites all existing objects in the Deque into a frame.

- Level - This class tracks which Notes should be displayed and displays them. It also
uses the Keymap in the Keyboard class to check whether Notes have been pressed
and iterates score. At the end of the game this class displays highscores and calculates
which scores are the highest scores in the text file scores.txt.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
- I spent about 5 hours trying to get the concurrent sound system working. Eventually
I had to go about asking for more Line objects from the system in a roundabout way
to ensure that I would not get a Line that was already in use.

- I had initially planned to take timing into account of score, but realized
that Laptop keyboards are pretty bad for accurate timing, especially when you're
sliding back and forth as this design requires. Hence now the scoring system 
just checks if you have hit the note, not whether if you're accurately hitting it.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
- I think this design is pretty good except for the Audio class. It is pretty hacky,
and I would definetly do more research into the Sound libraries to write it more
efficiently. I would also seperate the score functionality out of Level, as I
intended it more to be an object for a specific song. Eventually the game
would parse in different songs into different Level objects and then offer different
songs to play at startup...atleast that was the hope. If only I had more time. 



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
- Mostly just a lot of Java documentation.

- Osu map: https://osu.ppy.sh/beatmapsets/484689/#osu/1033882
- Osu file format: https://osu.ppy.sh/help/wiki/osu!_File_Formats/Osu_(file_format)
