<!-- BEGIN: Sticky Header 2-->
<div id="header_container">
    <div id="header">
        <img id="mini-logo" src="Content/images/header-logo.png" />
        <div id="header-nav">
            <span class="dropdown">
			    <span class="dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
			        {{name}}
			        <span class="caret"></span>
			    </span>
			    <ul class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="dropdownMenu1">
				    <li role="presentation"><a role="menuitem" tabindex="-1" ng-click="refresh()">Refresh</a></li>
				    <li role="presentation"><a role="menuitem" tabindex="-1">Manage Feeds</a></li>
				    <li role="presentation"><a role="menuitem" tabindex="-1">Settings</a></li>
				    <li role="presentation" class="divider"></li>
				    <li role="presentation"><a role="menuitem" tabindex="-1" ng-click="logout()">Log out</a></li>
				</ul>
			</span>
            <img id="settings" src="Content/images/settings.png" />
        </div>
    </div>
</div>
<!-- END: Sticky Header -->
 
<div id="feeds" class="container-fluid">
    <div class="row-fluid">
        <div id="sideBar" class="span1">
            <!--Sidebar content-->
            <ul id="categories">
                <li id="allFeeds" ng-click="displayFeedsForAllCategory()">All</li>
                <li ng-repeat="feedCategory in feedCategories">
                    <span ng-click="show_$index = ! show_$index">
                        <img src="${pageContext.request.contextPath}/Content/images/selector-right-arrow.png" ng-show="! show_$index"/>
                        <img src="${pageContext.request.contextPath}/Content/images/selector-down-arrow.png" ng-show="show_$index"/>
                    </span>
                    <span ng-click="displayFeedsForCategory(feedCategory.categoryId)">
                        {{ feedCategory.name }}
                    </span>
                    <ul id="feedContents_$index" class="feeds" ng-show="show_$index">
                        <li class="feed" ng-repeat="feed in feedCategory.feeds"  ng-click="displayFeedsForFeed(feed.feedId)">
                            {{feed.name}}
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <div id="main-content" class="span11">
            <!--Body content--> 
            <ul id="feed-list">
                <li class="feed-item" ng-repeat="feed in feeds" ng-class-odd="'odd'" ng-class-even="'even'">
                    <span ng-click="toggleArticle($index)">
                        <span class="feedSource" title="{{feed.source}}">{{feed.source}}</span>
                        <span class="feedTitle">{{ feed.title }}</span>
                        <span class="feedPubDate">{{feed.formattedDate}}</span>
                    </span>
                    <article ng-class="articleClass($index)">
	                    <!-- contrived reverse example--> 
	                    <h3>{{feed.title}}</h3>
	                    <span>{{feed.source}} {{feed.formattedDate}}</span>
	                    <div id="contents">
	                        {{ feed.description}} <a href="{{feed.link}}" target="new">Read More</a>
	                    </div>
                    </article>
                </li>
            </ul>
        </div> 
    </div>
</div>


<div id="footer_container">
    <div id="footer">
        Footer Content
    </div>
</div>

