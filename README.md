# Model View Anything

The project sample is representation of Different Architectures.<br/>
There are three architetures that leads to almost same use case but different implementation. <br/>
> MVP<br/> MVP-MVI<br/> MVVM-MVI<br/> 

<br/>**Model View Presenter.** <br/><br/>
**MVP to MVI:** That basically means how we can implement ModelViewIntent(MVI) from MVP. The use case is as same as MVP but, the viewState is used to represent the state of View and Intent/Events are used for Presentation Logic and Structured Concurrency for the cancellation of provided scope. <br/><br/>
**MVVM to MVI:** This module has same use case but with MVVM, and the implementation of this architecture (because of ViewModel) is the way way different than the presenter one, cause most of stuff handled by viewModel for us, not only the configuration changes but also the scope that viewModel provides us. <br/>
