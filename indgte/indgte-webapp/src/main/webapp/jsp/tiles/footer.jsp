<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<style>
.footer {
	min-height: 200px;
	text-align: center;
}
.footer-container {
	margin: 140px 0 0 0;
	vertical-align: bottom;
}
</style>

<section class="footer grid_12">
	<div class="footer-container">
		<div>
			<form:options items="${themes }" itemLabel="name" />
		</div>
		<div>Made by Mark</div>
	</div>
</section>