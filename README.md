## Expense tracker using jetpack compose UI
___

#### DESCRIPTION
> Expense tracker using jetpack compose UI, AndroidX, coroutines, view models & unit tests
> Ide version: Android Studio Artic Fox | 2020.3.1 Canary 12
> Kotlin Version: Update Channel Early Access Preview 1.4.x
___

#### REQUIREMENTS

###### GENERAL
> Use device locale for money formatting
> 3 account categories: cash, credit card, bank account 
> 5 expense categories: tax, grocery, entertainment, gym, health
> 2 income categories: income, expense
> category CRUD 
> data persist between launches

###### DASHBOARD SCREEN
> list of accounts
> group header: account name & balance 
> group list item: expense, date & amount
> today & yesterday date labels or dd/mm/yyyy
> time label hh:mm
> red text for expense, green for income
> list only last 10 transactions ( ORDER BY dateCreated DESC LIMIT 10)
> delete a transaction by swiping left to delete
> add transaction button navigates to add transaction screen

###### ADD TRANSACTION SCREEN
> select transaction type
> select account
> select category
> enter amount
> submit or cancel


###### UNIT TESTS
> see test directory