# PixelLuxe

[![Java CI with Maven](https://github.com/VanDulti/PixelLuxe/actions/workflows/pr.yml/badge.svg)](https://github.com/VanDulti/PixelLuxe/actions/workflows/pr.yml)
[![Deploy PixelLuxe Web to pages](https://github.com/VanDulti/PixelLuxe/actions/workflows/pages.yml/badge.svg)](https://github.com/VanDulti/PixelLuxe/actions/workflows/pages.yml)

## Introduction

PixelLuxe is a simple, lightweight, and easy-to-use image editor, coded in our Multimedia Systems class at JKU, that
allows you to edit images in a variety of ways.
It is designed to be user-friendly and intuitive, making it easy for anyone to use, regardless of their level of
experience with image editing software.

## Goals

In the beginning, we set out to create a simple image editor that would allow users to import and save images in JPEG
format, as well as perform basic image editing functions such as drawing and erasing. We also wanted to implement
various filters.

By the actual project submission, the plan was to have implemented all the following features:

- [x] Basic image editing functionality
    - [x] Import images (in JPEG format)
    - [x] Save images (in JPEG format)
    - [x] Drawing
    - [x] Erasing
- [x] Implement various filters
    - [x] Invert colors
    - [x] Adjust contrast
    - [x] Adjust saturation
    - [x] Convert to grayscale
- [x] Implement additional features such as undo/redo functionality (if time allows)
- [x] Create a user-friendly and intuitive interface (although this is of course a subjective goal)

In the end, we were able to implement all of the above features, as well as a few additional features that we had not
originally planned for.
Look at the following section for an overview of the actual functionality:

## Overview of available functionality

PixelLuxe supports most features needed in a basic image editor, with a special focus on filtering and doing convolution
operations on images.

The basic functionality includes importing and exporting images in not just JPEG (as originally planned), but also PNG
and many more.
The user can open & work on multiple images in a single session at the same time.
This is implemented in the form of tab-support, where each image is opened in a separate tab.

The user can draw on the image using a brush tool and erase parts of the image.
For analysis purposes, the user can open a histogram of the image to see the distribution of colors in the image.
This supports all available color channels of an image (RGB(A) and grayscale/average).

As opposed to the original plan, we did not just implement filters for inverting colors, adjusting contrast, adjusting
saturation, and converting to grayscale, but many more.
The user can apply a variety of filters to the image, such as blurring, sharpening, edge detection, and embossing, some
of them implemented as convolution operations on the image.
Regarding that, it is quite easy to add new filters and specifically also convolution operations to PixelLuxe,
especially if they are "just" regular convolution kernels applied without any additional properties (like some of the
existing filters need).

The undo/redo functionality was implemented as well, allowing the user to undo and redo any changes made to the image.
The only limitation is a limit to the number of tracked changes, that acts as a memory saving mechanism.
Multiple large images could otherwise quickly fill up the computer's memory.

For handling those large images, the user can also scale the image to a desired size, which is especially useful for
large images that are too big to be displayed on the screen.
The user can also drag the image around or move it via the keyboard.

As for the user interface, we tried to make it as user-friendly and intuitive as possible.
This includes a modern and clean design, as well as a simple and easy-to-understand layout.
The user can easily access all the features of the program through the menu bar at the top of the window.
Additionally, keyboard shortcuts are available for nearly all features.
The layout is similar to other image editing software, so users who are familiar with other programs should feel right
at home.

## Technologies used

### Java Swing

Multiple main technologies were thought of,
such [Swing](https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F/javax/swing/package-summary.html) or web technologies
like React or Angular.
In the end, multiple reasons led to the decision of using Java Swing.
Of course, Java Swing is not the most modern technology, but with the right approach and proper tooling, it can still be
used to create modern and user-friendly applications.
As Swing is Java, it is platform-independent (except for the web, but see the next section for that 😉).@

Most importantly, all team members are at least familiar with Java, and we're all right at the beginning of our studies,
so we thought it would be a good idea to stick to what we know.
Otherwise, the given total 20 hours of work per person would probably be spent entirely on failing a new technology,
instead of getting things done in Java.
Fittingly, Java Swing offers a - once again not that modern - very simple way to create a graphical user interface, with
the simplicity getting you started quickly.
Especially with [JDK 21](https://openjdk.org/projects/jdk/21/), the code can be quite clean and easy to understand, even
for beginners.

In the end, we were able to create a user-friendly and intuitive interface that is easy to use and understand (code &
program), implying that Java Swing was probably the right choice for this project.

### FlatLaf

To make the user interface look modern and clean, we used the [FlatLaf](https://www.formdev.com/flatlaf/) look and feel
library.
FlatLaf is a modern open-source look and feel library for Java Swing applications inspired by the popular Flat UI design
used in web applications & swing-specifically in IntelliJ IDEA.
Additionally, FlatLaf improves Swing's default customizability (can be limited an old-fashioned).

### Webapp

We also thought about creating a pure web application, but in the end, we decided against it.
Instead, we opted for an interesting new approach of porting Java applications to the web.
[CheerpJ](https://cheerpj.com), a browser-side JVM replacement allows to run most Java applications in the browser.

Some features do not yet work correctly in the web version of PixelLuxe, and the FlatLaf look and feel is not supported
there.
So while this web support is still very experimental, it is an interesting approach to make our application at
least easy to try out for everyone.

## Version Control, CI/CD & Code Quality

While we are definitely not experts (yet), we tried to use the best practices we learned and found in our personal
research.
That being said, we had to learn quite a lot to do things right.

### Git & GitHub

The consensus was pretty clear on this one: We should learn Git as soon as possible in our carrier. To host our git
repository, we chose GitHub, as it is most likely the most popular platform for hosting open-source projects like ours.
This meant learning about commits, branching, merging, pull requests, and more.

### CI/CD

We also wanted to learn about continuous integration and continuous deployment, so we set up a GitHub Actions workflow
to automatically build and test our project whenever we push changes to the repository. This way, we can quickly catch
any errors or bugs that may have been introduced by our changes, so that the main branch becomes an always correct
single source of truth for our code.
If a pull request is merged successfully, the changes are automatically deployed to the web version of PixelLuxe.

### Code Quality

Code quality was ensured by requiring code reviews from at least one other colleague.
Furthermore, using a shared .editorconfig file (supported by almost all IDEs) for consistent code formatting helped to
keep the code clean and readable.

## Structure

### `pixelluxe-gui`

This is the main module of the project, containing the Java GUI code for the image editor.

### `pixelluxe-web`

This module contains the html for the web version of PixelLuxe, which is still very experimental and not yet fully
functional.

## Installation

To run PixelLuxe, you need to have Java 21 & [Maven](https://maven.apache.org).
Aside from that, it's as simple as cloning, syncing the maven dependencies and running either `at.jku.pixelluxe.Main`
or `at.jku.pixelluxe.WebMain`.
Your IDE (like IntelliJ) should be able to handle all of this for you.

## Usage

Just try it out! Use [https://ulrich-dultinger.dev/PixelLuxe/](https://ulrich-dultinger.dev/PixelLuxe/) for a limited
web version of PixelLuxe.

## Plans for the future

PixelLuxe is built in a way that makes it easy to add new features and filters.

Possible future features include:

- [ ] More basic tools (selection-functionality is coming soon)
- [ ] Background removal using edge detection or AI
- [ ] More advanced filters such as noise reduction or color correction
- [ ] Support for a custom image format including advanced layering

## Conclusion

We are proud of our work on PixelLuxe, and it is a good feeling to know what one can achieve in a short amount of time (
20 hours per person after all).
This was not just one of our first projects, but also a lot of new experiences and learnings for all of us.
